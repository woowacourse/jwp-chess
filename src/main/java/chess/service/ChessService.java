package chess.service;

import chess.dao.BoardDao;
import chess.dao.RoomDao;
import chess.domain.Score;
import chess.domain.Team;
import chess.domain.piece.Blank;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.position.Position;
import chess.domain.state.*;
import chess.dto.request.GameIdRequest;
import chess.dto.request.RoomRequest;
import chess.dto.response.*;
import chess.entity.BoardEntity;
import chess.entity.RoomEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChessService {
    private final BoardDao chessBoardDao;
    private final RoomDao chessRoomDao;

    public ChessService(BoardDao chessBoardDao, RoomDao chessRoomDao) {
        this.chessBoardDao = chessBoardDao;
        this.chessRoomDao = chessRoomDao;
    }

    public void validateGameId(long id) throws SQLException {
        if (!chessRoomDao.isExistId(new GameIdRequest(id))) {
            throw new SQLException("존재하지 않는 게임아이디입니다.");
        }
    }

    public Long initializeGame(RoomRequest roomRequest) {
        RoomEntity room = chessRoomDao.findById(roomRequest);
        if (room == null) {
            chessRoomDao.makeGame(Team.WHITE, roomRequest);
            createGameState(chessRoomDao.findById(roomRequest).getId());
            room = chessRoomDao.findById(roomRequest);
        }
        initializeGameState(room);
        return chessRoomDao.findById(roomRequest).getId();
    }

    public void loadExistGame(long id) {
        RoomEntity room = chessRoomDao.findById(new GameIdRequest(id));
        initializeGameState(new RoomEntity(room.getId(), room.getStatus(), room.getName(), room.getPassword()));
    }

    private WhiteTurn createGameState(long roomId) {
        Map<Position, Piece> board = BoardInitialize.create();
        chessBoardDao.savePieces(board, roomId);
        return new WhiteTurn(board);
    }

    private GameState initializeGameState(RoomEntity room) {
        return room.getStatus().findStateByTeam(toBoard(room.getId()));
    }

    private Map<Position, Piece> toBoard(long id) {
        Map<Position, Piece> board = new HashMap<>();
        for (BoardEntity pieces : chessBoardDao.findAllPiece(id)) {
            board.put(Position.from(pieces.getPosition()), PieceFactory.create(pieces.getSymbol()));
        }
        return board;
    }

    public GameStateResponse move(Long id, MoveResponse moveResponse) {
        GameState gameState = updatePosition(id, moveResponse.getSource(), moveResponse.getDestination());
        updateState(gameState, id);
        return new GameStateResponse(gameState.team(), gameState.isRunning());
    }

    private GameState updatePosition(long id, String source, String destination) {
        Map<Position, Piece> board = toBoard(id);
        Piece sourcePiece = board.get(Position.from(source));
        GameState state = findPresentState(id).move(source, destination);

        chessBoardDao.updatePosition(Blank.SYMBOL, source, id);
        chessBoardDao.updatePosition(sourcePiece.getSymbol(), destination, id);
        return state;
    }

    private GameState findPresentState(long id) {
        RoomEntity room = chessRoomDao.findById(new GameIdRequest(id));
        return room.getStatus().findStateByTeam(toBoard(id));
    }

    private void updateState(GameState gameState, long id) {
        RoomEntity room = chessRoomDao.findById(new GameIdRequest(id));
        if (!gameState.isRunning()) {
            chessRoomDao.updateStatus(Team.NONE, id);
            return;
        }
        chessRoomDao.updateStatus(room.getStatus().nextTurn(), id);
    }

    public void resetBoard(long id) {
        chessRoomDao.updateStatus(Team.WHITE, id);
        chessBoardDao.deleteBoard(id);
        createGameState(id);
    }

    public void endGame(long id) {
        chessRoomDao.deleteGame(id);
    }

    public void updateStateEnd(long gameId) {
        chessRoomDao.updateStatus(Team.NONE, gameId);
    }

    public boolean isPossibleDeleteGame(Long id, String password) {
        RoomEntity room = chessRoomDao.findById(new GameIdRequest(id));
        return (room.getStatus().isEnd()) && (room.getPassword().equals(password));
    }

    public GameStateResponse findWinner(long id) {
        Score score = new Score(toBoard(id));
        Team winningTeam = score.getWinningTeam();
        RoomEntity room = chessRoomDao.findById(new GameIdRequest(id));
        return new GameStateResponse(winningTeam, room.getStatus().isEnd());
    }

    public BoardResponse getBoard(long id) {
        List<PieceResponse> pieces = toBoard(id).entrySet()
                .stream()
                .map(piece -> new PieceResponse(piece.getKey().toSymbol(), piece.getValue().getSymbol()))
                .collect(Collectors.toUnmodifiableList());

        RoomEntity room = chessRoomDao.findById(new GameIdRequest(id));
        return new BoardResponse(pieces, room.getStatus(), id);
    }

    public ScoreResponse getStatus(long id) {
        Score score = new Score(toBoard(id));
        return new ScoreResponse(score.getTotalScoreWhiteTeam(), score.getTotalScoreBlackTeam());
    }

    public List<RoomResponse> getGames() {
        List<RoomEntity> games = chessRoomDao.getGames();
        return games.stream()
                .map(roomEntity -> new RoomResponse(roomEntity.getId(), roomEntity.getStatus(),
                roomEntity.getName(), roomEntity.getPassword()))
                .collect(Collectors.toList());
    }
}
