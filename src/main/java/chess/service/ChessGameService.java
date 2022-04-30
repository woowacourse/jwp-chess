package chess.service;

import static chess.domain.piece.Team.BLACK;
import static chess.domain.piece.Team.WHITE;

import chess.dao.GameStateDao;
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
import chess.dto.RoomDto;
import chess.dto.ScoreDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Service
@Transactional(rollbackFor = Exception.class)
public class ChessGameService {

    private static final String EMPTY_GAME_STATE = "nothing";
    private static final String PLAYING_GAME_STATE = "playing";

    private static final Map<String, Function<Team, Piece>> PIECE_CREATION_STRATEGY_BY_NAME =
            Map.of("Pawn", Pawn::new, "King", King::new, "Queen", Queen::new,
                    "Rook", Rook::new, "Knight", Knight::new, "Bishop", Bishop::new);
    private static final Map<String, Team> TEAM_CREATION_STRATEGY = Map.of("WHITE", WHITE, "BLACK", BLACK);

    private final PieceDao pieceDao;
    private final GameStateDao gameStateDao;
    private final RoomDao roomDao;
    private final DataSource dataSource;

    public ChessGameService(final PieceDao pieceDao, final GameStateDao gameStateDao,
                            final RoomDao roomDao, final DataSource dataSource) {
        this.pieceDao = pieceDao;
        this.gameStateDao = gameStateDao;
        this.roomDao = roomDao;
        this.dataSource = dataSource;
    }

    public Map<Position, Piece> getPieces(final int roomNumber) {
        final String gameState = gameStateDao.getGameState(roomNumber);

        if (gameState.equals(EMPTY_GAME_STATE)) {
            return new HashMap<>();
        }

        return convertToPiecesByPosition(roomNumber);
    }

    public Map<Position, Piece> start(final int roomNumber) {
        validatePlayingGame(roomNumber);
        final Board board = new Board();
        final String turn = board.getTurn()
                .name();
        gameStateDao.saveTurn(roomNumber, turn);
        gameStateDao.saveState(roomNumber, PLAYING_GAME_STATE);

        pieceDao.saveAllPieces(roomNumber, board.getPieces());
        return board.getPieces();
    }

    private void validatePlayingGame(final int roomNumber) {
        if (gameStateDao.hasPlayingGame(roomNumber)) {
            throw new IllegalStateException("이미 진행 중인 게임이 있습니다.");
        }
    }

    public Map<Position, Piece> move(final int roomNumber, final String sourcePosition, final String targetPosition) {
        checkPlayingGame(roomNumber);
        Board board = getSavedBoard(roomNumber);
        final Board movedBoard = board.movePiece(Position.from(sourcePosition), Position.from(targetPosition));
        final Piece piece = movedBoard.getPieces().get(Position.from(targetPosition));

        pieceDao.removePieceByPosition(roomNumber, sourcePosition);
        pieceDao.removePieceByPosition(roomNumber, targetPosition);
        pieceDao.savePiece(roomNumber, targetPosition, piece);
        final Team turn = movedBoard.getTurn();
        gameStateDao.saveTurn(roomNumber, turn.name());
        return movedBoard.getPieces();
    }

    private void checkPlayingGame(final int roomNumber) {
        if (!gameStateDao.hasPlayingGame(roomNumber)) {
            throw new IllegalStateException("진행 중인 게임이 없습니다.");
        }
    }

    private Board getSavedBoard(final int roomNumber) {
        final String turn = gameStateDao.getTurn(roomNumber);
        final Team turnTeam = TEAM_CREATION_STRATEGY.get(turn);
        final Map<Position, Piece> pieces = convertToPiecesByPosition(roomNumber);
        return new Board(pieces, turnTeam);
    }

    private Map<Position, Piece> convertToPiecesByPosition(final int roomNumber) {
        final List<PieceDto> allPieces = pieceDao.findAllPieces(roomNumber);
        final Map<Position, Piece> pieces = new HashMap<>();

        for (PieceDto pieceDto : allPieces) {
            Position position = Position.from(pieceDto.getPosition());
            Team team = TEAM_CREATION_STRATEGY.get(pieceDto.getTeam());
            String name = pieceDto.getName();
            Piece piece = PIECE_CREATION_STRATEGY_BY_NAME.get(name)
                    .apply(team);
            pieces.put(position, piece);
        }
        return pieces;
    }

    public ScoreDto getScore(final int roomNumber) {
        checkPlayingGame(roomNumber);
        final Board board = getSavedBoard(roomNumber);
        return new ScoreDto(board.getTotalPoint(WHITE), board.getTotalPoint(BLACK));
    }

    public Map<Position, Piece> end(final int roomNumber) {
        final Board board = getSavedBoard(roomNumber);
        checkPlayingGame(roomNumber);
        gameStateDao.removeGameState(roomNumber);
        pieceDao.removeAllPieces(roomNumber);
        return board.getPieces();
    }

    public List<RoomDto> getRooms() {
        return roomDao.findAllRoom();
    }

    public List<RoomDto> createRoom(final String name, final String password) {
        roomDao.createRoom(name, password);
        return roomDao.findAllRoom();
    }

    public void deleteRoom(final int roomNumber, final String password) {
        final String currentGameState = gameStateDao.getGameState(roomNumber);
        validateRoom(roomNumber, password, currentGameState);
        pieceDao.removeAllPieces(roomNumber);
        gameStateDao.removeGameState(roomNumber);
        roomDao.deleteRoom(roomNumber, password);
    }

    private void validateRoom(int roomNumber, String password, String currentGameState) {
        if (currentGameState != null && currentGameState.equals("playing")) {
            throw new IllegalStateException("진행 중인 게임은 종료할 수 없습니다.");
        }
        if (!roomDao.checkRoom(roomNumber, password)) {
            throw new IllegalStateException("잘못된 비밀번호 입력입니다.");
        }
    }
}
