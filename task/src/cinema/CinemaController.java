package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static cinema.Cinema.getAllSeats;

import java.util.*;

@RestController
public class CinemaController {

    private Cinema cinema;
    public CinemaController(){
        this.cinema = getAllSeats(9, 9);
    }

    @GetMapping("/seats")
    public Cinema getSeats() {
        return cinema;
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchase(@RequestBody Seat seat) {
        if (seat.getColumn() > cinema.getTotal_columns()
                || seat.getRow() > cinema.getTotal_rows()
                || seat.getRow() < 1
                || seat.getColumn() < 1) {
            return new ResponseEntity<>(Map.of("error", "The number of a row or a column is out of bounds!"), HttpStatus.BAD_REQUEST);
        }
        for (int i = 0; i < cinema.getAvailable_seats().size(); i++) {
            Seat s = cinema.getAvailable_seats().get(i);
            if (s.equals(seat)) {
                OrderedSeat orderedSeat = new OrderedSeat(UUID.randomUUID(),s);
                cinema.getOrdered_seats().add(orderedSeat);
                cinema.getAvailable_seats().remove(i);
                return new ResponseEntity<>(orderedSeat, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(Map.of("error", "The ticket has been already purchased!"), HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/return")
    public ResponseEntity<?> returnSeats (@RequestBody Token token){
        List<OrderedSeat> orderedSeats = cinema.getOrdered_seats();
        for (OrderedSeat orderedSeat: orderedSeats) {
            if(orderedSeat.getToken().equals(token.getToken())){
                orderedSeats.remove(orderedSeat);
                cinema.getAvailable_seats().add(orderedSeat.getTicket());
                return new ResponseEntity<>(Map.of("returned_ticket",orderedSeat.getTicket()), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(Map.of("error", "Wrong token!"), HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/stats")
        public ResponseEntity<?> stats (@RequestParam(required = false) String password){
        if (password != null && password.equals("super_secret")){
            Map<String, Integer> statistics = new HashMap<>();
            int currentIncome = 0;
            for (OrderedSeat orderedSeat: cinema.getOrdered_seats()) {
                currentIncome+=orderedSeat.getTicket().getPrice();
            }
            int availaleSeats = cinema.getAvailable_seats().size();
            int purchasedSeats = cinema.getOrdered_seats().size();
            statistics.put("current_income", currentIncome);
            statistics.put("number_of_available_seats", availaleSeats);
            statistics.put("number_of_purchased_tickets", purchasedSeats);
            return new ResponseEntity<>(statistics, HttpStatus.OK);
        }
        return new ResponseEntity<>(Map.of("error", "The password is wrong!"), HttpStatus.valueOf(401));
    }

}


