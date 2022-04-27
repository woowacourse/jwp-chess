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
    private static final Map<String, Team> TEAM_CREATION_STRATEGY = Map.of("WHITE", WHITE, "BLACK", BLACK);

    private final PieceDao pieceDao;
    private final RoomDao roomDao;

    public ChessGameService(final PieceDao pieceDao, final RoomDao roomDao) {
        this.pieceDao = pieceDao;
        this.roomDao = roomDao;
    }

    public Map<Position, Piece> getPieces(final String roomName) {
        final int roomId = roomDao.getRoomId(roomName);
        final List<PieceDto> savedPieces = pieceDao.findPiecesByRoomIndex(roomId);
        final Map<Position, Piece> pieces = convert(savedPieces);
        return pieces;
    }

    private Map<Position, Piece> convert(final List<PieceDto> savedPieces) {
        final Map<Position, Piece> pieces = new HashMap<>();
        for (PieceDto pieceDto : savedPieces) {
            Position position = Position.from(pieceDto.getPosition());
            Team team = TEAM_CREATION_STRATEGY.get(pieceDto.getTeam());
            String name = pieceDto.getName();
            Piece piece = PIECE_CREATION_STRATEGY_BY_NAME.get(name)
                    .apply(team);
            pieces.put(position, piece);
        }
        return pieces;
    }

    public void start(final String roomName) {
        final int roomId = roomDao.getRoomId(roomName);
        roomDao.saveGameState(roomName, "playing");

        final Board board = new Board();
        final Map<Position, Piece> pieces = board.getPieces();
        pieceDao.saveAllPieces(roomId, pieces);
    }

    public void move(final String roomName, final String sourcePosition, final String targetPosition) {
        checkGameIsPlaying(roomName);
        final Board board = getSavedBoard(roomName);
        final Board movedBoard = board.movePiece(Position.from(sourcePosition), Position.from(targetPosition));
        final Piece piece = movedBoard.getPieces().get(Position.from(targetPosition));
        roomDao.saveTurn(roomName, movedBoard.getTurn().toString());

        final int roomId = roomDao.getRoomId(roomName);
        pieceDao.removePiece(roomId, sourcePosition);
        pieceDao.removePiece(roomId, targetPosition);
        pieceDao.savePiece(roomId, targetPosition, piece);
    }

    public ScoreDto getScore(final String roomName) {
        checkGameIsPlaying(roomName);
        final Board board = getSavedBoard(roomName);
        return new ScoreDto(board.getTotalPoint(WHITE), board.getTotalPoint(BLACK));
    }

    private void checkGameIsPlaying(final String roomName) {
        final String gameState = roomDao.getGameStateByName(roomName);
        if (!gameState.equals("playing")) {
            throw new IllegalStateException("진행중인 게임이 없습니다.");
        }
    }

    public void end(final String roomName) {
        checkGameIsPlaying(roomName);
        final int roomId = roomDao.getRoomId(roomName);
        pieceDao.removeAllPieces(roomId);
    }

    private Board getSavedBoard(final String roomName) {
        final String turn = roomDao.getTurn(roomName);
        final Team turnTeam = TEAM_CREATION_STRATEGY.get(turn);
        final Map<Position, Piece> pieces = getPieces(roomName);
        return new Board(pieces, turnTeam);
    }
}
