package chess.service;

import chess.model.MoveType;
import chess.model.Turn;
import chess.model.board.Board;
import chess.model.dao.GameDao;
import chess.model.dao.PieceDao;
import chess.model.dto.MoveDto;
import chess.model.dto.WebBoardDto;
import chess.model.piece.Empty;
import chess.model.piece.Piece;
import chess.model.piece.PieceFactory;
import chess.model.position.Position;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChessMoveService {

    private final PieceDao pieceDao;
    private final GameDao gameDao;
    private final ChessBoardService chessBoardService;

    public ChessMoveService(PieceDao pieceDao, GameDao gameDao, ChessBoardService chessBoardService) {
        this.pieceDao = pieceDao;
        this.gameDao = gameDao;
        this.chessBoardService = chessBoardService;
    }

    public WebBoardDto move(Long gameId, MoveDto moveDto) {
        String srcPosition = moveDto.getSource();
        String dstPosition = moveDto.getTarget();
        Piece sourcePiece = PieceFactory.create(pieceDao.findNameByPositionAndGameId(srcPosition, gameId));
        Piece targetPiece = PieceFactory.create(pieceDao.findNameByPositionAndGameId(dstPosition, gameId));

        return move(gameId, srcPosition, dstPosition, sourcePiece, targetPiece);
    }

    public WebBoardDto move(Long gameId, String srcPosition, String dstPosition, Piece sourcePiece, Piece targetPiece) {
        Turn turn = Turn.from(chessBoardService.getTurn(gameId));
        validateCurrentTurn(turn, sourcePiece);

        movePiece(gameId, srcPosition, dstPosition, sourcePiece, targetPiece, turn);

        Board board = chessBoardService.toBoard(pieceDao.findAllPiecesByGameId(gameId));
        checkKingDead(gameId, turn, board);

        return WebBoardDto.from(board);
    }

    private void validateCurrentTurn(Turn thisTurn, Piece sourcePiece) {
        if (!sourcePiece.isCurrentTurn(thisTurn)) {
            throw new IllegalArgumentException("본인의 말을 움직여야 합니다.");
        }
    }

    private void movePiece(Long gameId, String srcPosition, String dstPosition,
                           Piece sourcePiece, Piece targetPiece, Turn turn) {
        if (canMove(gameId, srcPosition, dstPosition, sourcePiece, targetPiece)) {
            pieceDao.updateByPositionAndGameId(PieceDao.getPieceName(sourcePiece), dstPosition, gameId);
            pieceDao.updateByPositionAndGameId("none-.", srcPosition, gameId);
            gameDao.updateTurnByGameId(gameId, turn.change().getThisTurn());
            return;
        }
        throw new IllegalArgumentException("기물을 이동할 수 없습니다.");
    }

    private boolean canMove(Long gameId, String srcPosition, String dstPosition, Piece sourcePiece, Piece targetPiece) {
        Position sourcePosition = Position.from(srcPosition);
        Position targetPosition = Position.from(dstPosition);
        MoveType moveType = MoveType.of(sourcePiece, targetPiece);

        return sourcePiece.isMovable(sourcePosition, targetPosition, moveType)
                && !hasBlock(gameId, sourcePosition, targetPosition, sourcePiece);
    }

    private boolean hasBlock(Long gameId, Position sourcePosition, Position targetPosition, Piece sourcePiece) {
        Board board = chessBoardService.toBoard(pieceDao.findAllPiecesByGameId(gameId));
        List<Position> positions = sourcePiece.getIntervalPosition(sourcePosition, targetPosition);

        return positions.stream()
                .anyMatch(position -> !board.get(position).equals(new Empty()));
    }

    private void checkKingDead(Long gameId, Turn turn, Board board) {
        if (board.isKingDead()) {
            gameDao.updateTurnByGameId(gameId, "end");
        }
    }
}
