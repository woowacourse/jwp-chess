package chess.web.service;

import chess.web.dao.PlayerRepository;
import chess.web.dto.PlayerDto;
import java.util.Optional;

public class PlayerService {

    private static final int NAME_MIN_SIZE = 1;
    private static final int NAME_MAX_SIZE = 12;
    private static final String ERROR_NAME_SIZE = "닉네임 길이는 1자 이상, 12자 이하입니다.";

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public PlayerDto login(String name) {
        validateNameSize(name);
        Optional<PlayerDto> playerDto = playerRepository.find(name);
        if (playerDto.isEmpty()) {
            playerRepository.save(name);
        }
        return playerRepository.find(name).get();
    }

    private void validateNameSize(String name) {
        if (name.length() < NAME_MIN_SIZE || name.length() > NAME_MAX_SIZE) {
            throw new IllegalArgumentException(ERROR_NAME_SIZE);
        }
    }
}
