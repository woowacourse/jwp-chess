package chess.service;

import static chess.domain.piece.Team.BLACK;
import static chess.domain.piece.Team.WHITE;

import chess.dao.PieceDao;
import chess.dao.RoomDao;
import chess.domain.board.Board;
import chess.domain.board.position.Position;
import chess.domain.piece.Bishop;
import chess.domain.piece.King;
import chess.domain.piece.Knight;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.Queen;
import chess.domain.piece.Rook;
import chess.domain.piece.Team;
import chess.dto.PieceDto;
import chess.dto.ScoreDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {

    private static final Map<String, Function<Team, Piece>> PIECE_CREATION_STRATEGY_BY_NAME =
            Map.of("Pawn", Pawn::new, "King", King::new, "Queen", Queen::new,
                    "Rook", Rook::new, "Knight", Knight::new, "Bishop", Bishop::new);
    private static final String PLAYING_STATE_VALUE = "playing";

    private final PieceDao pieceDao;
    private final RoomDao roomDao;

    public ChessGameService(final PieceDao pieceDao, final RoomDao roomDao) {
        this.pieceDao = pieceDao;
        this.roomDao = roomDao;
    }

    public Map<Position, Piece> getPieces(final int roomId) {
        final List<PieceDto> savedPieces = pieceDao.findPieces(roomId);
        return convertToPiece(savedPieces);
    }

    private Map<Position, Piece> convertToPiece(final List<PieceDto> savedPieces) {
        final Map<Position, Piece> pieces = new HashMap<>();
        for (PieceDto pieceDto : savedPieces) {
            Position position = Position.from(pieceDto.getPosition());
            Team team = Team.from(pieceDto.getTeam());
            String name = pieceDto.getName();
            Piece piece = PIECE_CREATION_STRATEGY_BY_NAME.get(name)
                    .apply(team);
            pieces.put(position, piece);
        }
        return pieces;
    }

    public void start(final int roomId) {
        checkGameIsAlreadyPlaying(roomId);
        roomDao.saveGameState(roomId, PLAYING_STATE_VALUE);
        final Board board = new Board();
        final Map<Position, Piece> pieces = board.getPieces();
        pieceDao.saveAllPieces(roomId, pieces);
    }

    private void checkGameIsAlreadyPlaying(final int roomId) {
        final String gameState = roomDao.getGameStateByName(roomId);
        if (gameState.equals(PLAYING_STATE_VALUE)) {
            throw new IllegalStateException("이미 진행중인 게임이 있습니다.");
        }
    }

    public void move(final int roomId, final String sourcePosition, final String targetPosition) {
        checkGameIsPlaying(roomId);
        final Board board = getSavedBoard(roomId);
        checkGameIsOver(board);
        final Board movedBoard = board.movePiece(Position.from(sourcePosition), Position.from(targetPosition));
        final Piece piece = movedBoard.getPieces().get(Position.from(targetPosition));
        roomDao.saveTurn(roomId, movedBoard.getTurn().toString());

        pieceDao.removePiece(roomId, sourcePosition);
        pieceDao.removePiece(roomId, targetPosition);
        pieceDao.savePiece(roomId, targetPosition, piece);
    }

    private void checkGameIsOver(final Board board) {
        if (board.hasOneKing()) {
            throw new IllegalStateException("King이 죽어 게임이 종료되었습니다.");
        }
    }

    public ScoreDto getScore(final int roomId) {
        checkGameIsPlaying(roomId);
        final Board board = getSavedBoard(roomId);
        return new ScoreDto(board.getTotalPoint(WHITE), board.getTotalPoint(BLACK));
    }

    private void checkGameIsPlaying(final int roomId) {
        final String gameState = roomDao.getGameStateByName(roomId);
        if (!gameState.equals(PLAYING_STATE_VALUE)) {
            throw new IllegalStateException("진행중인 게임이 없습니다.");
        }
    }

    public void end(final int roomId) {
        checkGameIsPlaying(roomId);
        pieceDao.removeAllPieces(roomId);
        roomDao.saveGameState(roomId, "ready");
        roomDao.saveTurn(roomId, "WHITE");
    }

    private Board getSavedBoard(final int roomId) {
        final String turn = roomDao.getTurn(roomId);
        final Team turnTeam = Team.from(turn);
        final Map<Position, Piece> pieces = getPieces(roomId);
        return new Board(pieces, turnTeam);
    }
}
