package muro.room.booking.controller;

import muro.room.booking.entity.Booking;
import muro.room.booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingsController {

    @Autowired
    BookingRepository bookingRepository;

    @GetMapping("/{date}")
    public List<Booking> getBookingByDate(@PathVariable("date") String date) {
        Date sqlDate = Date.valueOf(date);
        return bookingRepository.findAllByDate(sqlDate);
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable("id") Long id) {
        bookingRepository.deleteById(id);
    }

}
