package muro.room.booking.config;

import muro.room.booking.service.JWTService;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JWTFilter extends BasicAuthenticationFilter {

    private static JWTService jwtService;

    public JWTFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Cookie[] cookies = request.getCookies();

        if (cookies == null || cookies.length == 0) {
            chain.doFilter(request, response);
            return;
        }

        Cookie tokenCookie = Arrays.asList(cookies)
                .stream()
                .filter(c -> c.getName().equals("token"))
                .findFirst()
                .orElse(null);

        //String header = request.getHeader("Authorization");
        //if(header == null || !header.startsWith("Bearer"))
        if(tokenCookie == null){
            chain.doFilter(request, response);
            return;
        }

        if(jwtService == null) {
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            jwtService = webApplicationContext.getBean(JWTService.class);
        }

        //UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(header);
        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(tokenCookie.getValue());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        //String token = header.substring(7);
        try {
            String payload = jwtService.validateToken(token);
            JsonParser parser = JsonParserFactory.getJsonParser();
            Map<String, Object> payloadMap = parser.parseMap(payload);

            String user = payloadMap.get("user").toString();
            String role = payloadMap.get("role").toString();

            List<GrantedAuthority> roles = new ArrayList<>();
            GrantedAuthority grantedAuthority = new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return "ROLE_"+role;
                }
            };

            roles.add(grantedAuthority);

            return new UsernamePasswordAuthenticationToken(user, null, roles);
        } catch (Exception e) {
            return null; // Token is not valid
        }
    }
}
