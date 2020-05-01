package wooteco.chess.domain.board.initializer;

import java.util.HashMap;
import java.util.Map;

import wooteco.chess.domain.board.BoardInitializer;
import wooteco.chess.domain.piece.PieceState;
import wooteco.chess.domain.position.Position;

public class EnumRepositoryBoardInitializer implements BoardInitializer {

    @Override
    public Map<Position, PieceState> create() {
        Map<Position, PieceState> initialBoard = new HashMap<>();
        for (InitialPieceRepository pieceRepository : InitialPieceRepository.values()) {
            initialBoard.put(pieceRepository.getPosition(), pieceRepository.getInitialPiece());
        }
        return initialBoard;
    }
}
