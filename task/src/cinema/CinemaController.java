package cinema;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CinemaController {



    @GetMapping("/seats")
    public Cinema getSeats() {
        return getAllSeats();
    }
}


