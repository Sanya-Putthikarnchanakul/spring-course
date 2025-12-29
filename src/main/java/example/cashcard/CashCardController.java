package example.cashcard;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {

    private final CashCardRepository cashCardRepository;

    public CashCardController(CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }

    @GetMapping("/{requestedId}")
    private ResponseEntity<CashCard> findById(@PathVariable Long requestedId) {
        Optional<CashCard> optCashCard = cashCardRepository.findById(requestedId);

        if (optCashCard.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.ok(optCashCard.get());
    }

    @PostMapping
    private ResponseEntity<Void> createCashCard(@RequestBody CashCard request, UriComponentsBuilder uriComponentsBuilder) {

        CashCard createdCashCard = cashCardRepository.save(request);

        URI locationOfCreatedCashCard = uriComponentsBuilder
                .path("/cashcards/{id}")
                .buildAndExpand(createdCashCard.id())
                .toUri();

        return ResponseEntity.created(locationOfCreatedCashCard).build();
    }

}
