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
import chess.dto.request.MakeRoomRequest;
import chess.dto.response.*;
import org.springframework.stereotype.Service;

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

    public Long initializeGame(MakeRoomRequest makeRoomRequest) {
        RoomStatusResponse room = chessRoomDao.findById(makeRoomRequest);
        if (room == null) {
            chessRoomDao.makeGame(Team.WHITE, makeRoomRequest);
            createGameState(chessRoomDao.findById(makeRoomRequest).getId());
            room = chessRoomDao.findById(makeRoomRequest);
        }
        initializeGameState(room);
        return chessRoomDao.findById(makeRoomRequest).getId();
    }

    public void loadExistGame(long id) {
        RoomResponse room = chessRoomDao.findById(new GameIdRequest(id));
        initializeGameState(new RoomStatusResponse(room.getId(), room.status()));
    }

    private WhiteTurn createGameState(long roomId) {
        Map<Position, Piece> board = BoardInitialize.create();
        chessBoardDao.savePieces(board, roomId);
        return new WhiteTurn(board);
    }

    private GameState initializeGameState(RoomStatusResponse room) {
        return room.status().findStateByTeam(toBoard(room.getId()));
    }

    private Map<Position, Piece> toBoard(long id) {
        Map<Position, Piece> board = new HashMap<>();
        for (PieceResponse pieceOfPieces : chessBoardDao.findAllPiece(id)) {
            board.put(Position.from(pieceOfPieces.getPosition()), PieceFactory.create(pieceOfPieces.getSymbol()));
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
        RoomResponse room = chessRoomDao.findById(new GameIdRequest(id));
        return room.status().findStateByTeam(toBoard(id));
    }

    private void updateState(GameState gameState, long id) {
        RoomResponse room = chessRoomDao.findById(new GameIdRequest(id));
        if (!gameState.isRunning()) {
            chessRoomDao.updateStatus(Team.NONE, id);
            return;
        }
        chessRoomDao.updateStatus(room.status().nextTurn(), id);
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
        RoomResponse room = chessRoomDao.findById(new GameIdRequest(id));
        return (room.status().isEnd()) && (room.getPassword().equals(password));
    }

    public GameStateResponse findWinner(long id) {
        Score score = new Score(toBoard(id));
        Team winningTeam = score.getWinningTeam();
        RoomResponse room = chessRoomDao.findById(new GameIdRequest(id));
        return new GameStateResponse(winningTeam, room.status().isEnd());
    }

    public BoardResponse getBoard(long id) {
        List<PieceResponse> pieces = toBoard(id).entrySet()
                .stream()
                .map(piece -> new PieceResponse(piece.getKey().toSymbol(), piece.getValue().getSymbol()))
                .collect(Collectors.toUnmodifiableList());

        RoomResponse room = chessRoomDao.findById(new GameIdRequest(id));
        return new BoardResponse(pieces, room.status(), id);
    }

    public ScoreResponse getStatus(long id) {
        Score score = new Score(toBoard(id));
        return new ScoreResponse(score.getTotalScoreWhiteTeam(), score.getTotalScoreBlackTeam());
    }

    public List<RoomResponse> getGames() {
        return chessRoomDao.getGames();
    }
}
