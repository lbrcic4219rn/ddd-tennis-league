package com.github.lbrcic4219rn.dddtennisleague.application.player;

import com.github.lbrcic4219rn.dddtennisleague.application.player.dto.PlayerDto;
import com.github.lbrcic4219rn.dddtennisleague.domain.player.ContactInformation;
import com.github.lbrcic4219rn.dddtennisleague.domain.player.Player;
import com.github.lbrcic4219rn.dddtennisleague.domain.player.PlayerId;
import com.github.lbrcic4219rn.dddtennisleague.domain.player.PlayerRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerApplicationService {
    private final PlayerRepo playerRepo;

    public PlayerApplicationService(PlayerRepo playerRepo) {
        this.playerRepo = playerRepo;
    }

    public PlayerId registerPlayer(String firstName, String lastName, String email, String phoneNumber, LocalDate dateOfBirth) {
        ContactInformation contactInfo = new ContactInformation(email, phoneNumber);
        Player player = new Player(firstName, lastName, contactInfo, dateOfBirth);
        playerRepo.save(player);
        return player.getId();
    }

    public PlayerDto getPlayerById(PlayerId playerId) {
        Optional<Player> player = playerRepo.findById(playerId);
        return player.map(this::convertToDto).orElse(null);
    }

    public List<PlayerDto> getAllPlayers() {
        return playerRepo.findAll()
                .values()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void removePlayer(PlayerId playerId) {
        playerRepo.remove(playerId);
    }

    private PlayerDto convertToDto(Player player) {
        return new PlayerDto(
                player.getId().value().toString(),
                player.getFirstName(),
                player.getLastName(),
                player.getContactInfo().email(),
                player.getContactInfo().phoneNumber(),
                player.getDateOfBirth(),
                player.getRegisteredAt()
        );
    }
}

