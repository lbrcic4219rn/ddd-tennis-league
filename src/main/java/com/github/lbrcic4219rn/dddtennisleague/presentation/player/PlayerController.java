package com.github.lbrcic4219rn.dddtennisleague.presentation.player;

import com.github.lbrcic4219rn.dddtennisleague.application.player.PlayerApplicationService;
import com.github.lbrcic4219rn.dddtennisleague.application.player.dto.PlayerDto;
import com.github.lbrcic4219rn.dddtennisleague.domain.player.id.PlayerId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/players")
public class PlayerController {
    private final PlayerApplicationService playerService;

    @PostMapping
    public ResponseEntity<String> registerPlayer(@RequestBody RegisterPlayerRequest request) {
        PlayerId playerId = playerService.registerPlayer(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.phoneNumber(),
                request.dateOfBirth()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(playerId.value().toString());
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerDto> getPlayer(@PathVariable String playerId) {
        PlayerDto player = playerService.getPlayerById(new PlayerId(UUID.fromString(playerId)));
        if (player == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(player);
    }

    @GetMapping
    public ResponseEntity<List<PlayerDto>> getAllPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    @DeleteMapping("/{playerId}")
    public ResponseEntity<Void> removePlayer(@PathVariable String playerId) {
        playerService.removePlayer(new PlayerId(UUID.fromString(playerId)));
        return ResponseEntity.noContent().build();
    }

    public record RegisterPlayerRequest(
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            LocalDate dateOfBirth
    ) {
    }
}

