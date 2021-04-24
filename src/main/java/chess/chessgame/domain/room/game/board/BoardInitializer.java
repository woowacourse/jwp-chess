package chess.chessgame.domain.room.game.board;

import java.util.List;

public interface BoardInitializer {
    Board createBoard(List<Square> squares);
}
