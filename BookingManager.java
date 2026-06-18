package movieticketbookingsystem;

import movieticketbookingsystem.enums.PaymentStatus;
import movieticketbookingsystem.entities.*;
import movieticketbookingsystem.strategy.payment.PaymentStrategy;

import java.util.List;
import java.util.Optional;;

public class BookingManager{
    private final SeatLockManager SeatLockManager;
    public BookingManager(SeatLockManager seatLockManager){
        this.seatLockManager=seatLockManager;
    }

    public Optional<Booking> createBooking(User user,Show show,List<Seat> seats, PaymentStrategy PaymentStrategy){

        seatLockManager.lockSeats(show,seats,user.getId());

        double totalAmount=show.getPricingStartegy().calculatePrice(seats);

        Payment payment=paymentStrategy.pay(totalAmount);
    
        if(payment.getStatus()==PaymentStatus.SUCCESS){
            Booking booking = new Booking.BookingBuilder()
                    .setUser(user)
                    .setShow(show)
                    .setSeats(seats)
                    .setTotlaAmount(totalAmount)
                    .build()

            booking.confirmBooking();

            seatLockManager.unlockSetas(show,seats,user.getId());

            return Optional.of(booking);
        }else{
            System.out.println("Payment failed. Please try again");
            return Optional.empty();
        }
    }

}