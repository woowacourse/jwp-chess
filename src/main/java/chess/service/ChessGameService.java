package chess.service;

import static chess.domain.piece.Team.BLACK;
import static chess.domain.piece.Team.WHITE;

import chess.dao.PieceDao;
import chess.dao.RoomDao;
import chess.domain.board.Board;
import chess.domain.board.position.Position;
import chess.domain.piece.Piece;
import chess.domain.piece.Team;
import chess.dto.PieceDto;
import chess.dto.ScoreDto;
import chess.entity.Room;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {

    private static final String PLAYING_STATE_VALUE = "playing";

    private final PieceDao pieceDao;
    private final RoomDao roomDao;

    public ChessGameService(final PieceDao pieceDao, final RoomDao roomDao) {
        this.pieceDao = pieceDao;
        this.roomDao = roomDao;
    }

    public Map<Position, Piece> getPieces(final int roomId) {
        final List<PieceDto> savedPieces = pieceDao.findPieces(roomId);
        return Board.convertToPiece(savedPieces);
    }

    public void start(final int roomId) {
        checkGameIsNotPlaying(roomId);
        roomDao.saveGameState(roomId, PLAYING_STATE_VALUE);
        final Board board = new Board();
        final Map<Position, Piece> pieces = board.getPieces();
        pieceDao.saveAllPieces(roomId, pieces);
    }

    private void checkGameIsNotPlaying(final int roomId) {
        final Room room = roomDao.findByRoomId(roomId);
        room.checkGameIsNotPlaying();
    }

    public void move(final int roomId, final String sourcePosition, final String targetPosition) {
        checkGameIsPlaying(roomId);

        final Board board = getSavedBoard(roomId);
        final Board movedBoard = board.movePiece(Position.from(sourcePosition), Position.from(targetPosition));
        final Piece piece = movedBoard.getPieces().get(Position.from(targetPosition));
        roomDao.saveTurn(roomId, movedBoard.getTurn().toString());

        pieceDao.removePiece(roomId, sourcePosition);
        pieceDao.removePiece(roomId, targetPosition);
        pieceDao.savePiece(roomId, targetPosition, piece);
    }

    public ScoreDto getScore(final int roomId) {
        checkGameIsPlaying(roomId);
        final Board board = getSavedBoard(roomId);
        return new ScoreDto(board.getTotalPoint(WHITE), board.getTotalPoint(BLACK));
    }

    private void checkGameIsPlaying(final int roomId) {
        final Room room = roomDao.findByRoomId(roomId);
        room.checkGameIsPlaying();
    }

    public void end(final int roomId) {
        checkGameIsPlaying(roomId);
        pieceDao.removeAllPieces(roomId);
        roomDao.saveGameState(roomId, "ready");
        roomDao.saveTurn(roomId, "WHITE");
    }

    private Board getSavedBoard(final int roomId) {
        final String turn = roomDao.findByRoomId(roomId)
                .getTurn();
        final Team turnTeam = Team.from(turn);
        final Map<Position, Piece> pieces = getPieces(roomId);
        return new Board(pieces, turnTeam);
    }
}
