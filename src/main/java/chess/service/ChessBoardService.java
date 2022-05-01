package chess.service;

import chess.entity.PieceEntity;
import chess.model.GameResult;
import chess.model.board.Board;
import chess.model.dao.GameDao;
import chess.model.dao.PieceDao;
import chess.model.piece.PieceFactory;
import chess.model.position.Position;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ChessBoardService {

    private final PieceDao pieceDao;
    private final GameDao gameDao;

    public ChessBoardService(PieceDao pieceDao, GameDao gameDao) {
        this.pieceDao = pieceDao;
        this.gameDao = gameDao;
    }

    public static Board toBoard(List<PieceEntity> rawBoard) {
        return new Board(rawBoard.stream()
                .collect(Collectors.toMap(
                        piece -> Position.from(piece.getPosition()),
                        piece -> PieceFactory.create(piece.getName()))
                ));
    }

    public boolean isKingDead(Long gameId) {
        Board board = toBoard(pieceDao.findAllPiecesByGameId(gameId));
        return board.isKingDead();
    }

    public GameResult getResult(Long gameId) {
        Board board = toBoard(pieceDao.findAllPiecesByGameId(gameId));
        return GameResult.from(board);
    }
}