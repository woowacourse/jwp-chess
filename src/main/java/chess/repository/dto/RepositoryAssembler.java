package chess.repository.dto;

import chess.repository.dao.dto.game.GameStatusDto;
import chess.repository.dto.game.GameStatus;

public class RepositoryAssembler {

    private RepositoryAssembler() {
    }

    public static GameStatus gameStatus(final GameStatusDto gameStatusDto) {
        return new GameStatus(gameStatusDto.getId(), gameStatusDto.getTitle(), gameStatusDto.getFinished());
    }
}
