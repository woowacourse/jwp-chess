package chess.chessgame.domain.manager;

import chess.controller.web.dto.PieceDto;
import chess.chessgame.domain.board.Board;
import chess.chessgame.domain.piece.attribute.Color;
import chess.chessgame.domain.position.Position;
import chess.chessgame.domain.statistics.ChessGameStatistics;

import java.util.Map;

public interface ChessGameManager {
    ChessGameManager start();

    ChessGameManager end();

    void move(Position from, Position to);

    Board getBoard();

    Color nextColor();

    boolean isNotEnd();

    boolean isEnd();

    boolean isStart();

    boolean isKingDead();

    long getId();

    Map<String, PieceDto> getPieces();

    ChessGameStatistics getStatistics();
}
