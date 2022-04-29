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
    private GameState gameState = new WhiteTurn(new HashMap<>());

    public ChessService(BoardDao chessBoardDao, RoomDao chessRoomDao) {
        this.chessBoardDao = chessBoardDao;
        this.chessRoomDao = chessRoomDao;
    }

    public Long makeGame(MakeRoomRequest makeRoomRequest) {
        RoomStatusResponse room = chessRoomDao.findById(makeRoomRequest);
        if (room == null) {
            createGame(makeRoomRequest);
            gameState = createGameState(chessRoomDao.findById(makeRoomRequest).getId());
            room = chessRoomDao.findById(makeRoomRequest);
        }
        gameState = initGameState(room);
        return chessRoomDao.findById(makeRoomRequest).getId();
    }

    public void loadExistGame(long id) {
        RoomResponse room = chessRoomDao.findById(new GameIdRequest(id));
        gameState = initGameState(new RoomStatusResponse(room.getId(), room.getStatus()));
    }

    private void createGame(MakeRoomRequest makeRoomRequest) {
        chessRoomDao.makeGame(Team.WHITE, makeRoomRequest);
    }

    private WhiteTurn createGameState(long roomId) {
        Map<Position, Piece> board = BoardInitialize.create();
        chessBoardDao.savePieces(board, roomId);
        return new WhiteTurn(board);
    }

    private Playing initGameState(RoomStatusResponse room) {
        Map<Position, Piece> board = new HashMap<>();
        for (PieceResponse pieceOfPieces : chessBoardDao.findAllPiece(room.getId())) {
            board.put(Position.from(pieceOfPieces.getPosition()), PieceFactory.create(pieceOfPieces.getSymbol()));
        }

        Team status = room.getStatus();
        if (status.isWhiteTeam()) {
            return new WhiteTurn(board);
        }
        return new BlackTurn(board);
    }

    public GameStateResponse move(Long id, MoveResponse moveResponse) {
        updateMove(id, moveResponse.getSource(), moveResponse.getDestination());
        GameState gameState = changeState(id);
        Team team = gameState.getTeam();
        return new GameStateResponse(team, gameState.isRunning());
    }

    private void updateMove(long roomId, String source, String destination) {
        Map<Position, Piece> board = gameState.getBoard();
        Piece sourcePiece = board.get(Position.from(source));
        gameState = gameState.move(source, destination);

        chessBoardDao.updatePosition(Blank.SYMBOL, source, roomId);
        chessBoardDao.updatePosition(sourcePiece.getSymbol(), destination, roomId);
    }

    private GameState changeState(long roomId) {
        if (!gameState.isRunning()) {
            chessRoomDao.deleteGame(roomId);
            return gameState;
        }
        chessRoomDao.updateStatus(gameState.getTeam(), roomId);
        return gameState;
    }

    public void resetBoard(long id) {
        chessRoomDao.updateStatus(Team.WHITE, id);
        chessBoardDao.deleteBoard(id);
        gameState = createGameState(id);
    }

    public void endGame(long gameId) {
        gameState = new Finished(gameState.getTeam(), gameState.getBoard());
        chessRoomDao.deleteGame(gameId);
    }

    public void updateEndStatus(long gameId) {
        gameState = new Finished(gameState.getTeam(), gameState.getBoard());
        chessRoomDao.updateStatus(Team.NONE, gameId);
    }

    public boolean isPossibleDeleteGame(Long id, String password) {
        RoomResponse room = chessRoomDao.findById(new GameIdRequest(id));
        return (room.getStatus().equals(Team.NONE)) && (room.getPassword().equals(password));
    }

    public GameStateResponse findWinner() {
        Score score = new Score(gameState.getBoard());
        Team winningTeam = score.getWinningTeam();
        return new GameStateResponse(winningTeam, gameState.isRunning());
    }

    public RoomResponse findRoom(long id) {
        return chessRoomDao.findById(new GameIdRequest(id));
    }

    public BoardResponse getBoard(long id) {
        List<PieceResponse> pieces = toBoardFormat().entrySet()
                .stream()
                .map(piece -> new PieceResponse(piece.getKey(), piece.getValue()))
                .collect(Collectors.toUnmodifiableList());

        return new BoardResponse(pieces, gameState.getTeam(), id);
    }

    public ScoreResponse getStatus() {
        Score score = new Score(gameState.getBoard());
        return new ScoreResponse(score.getTotalScoreWhiteTeam(), score.getTotalScoreBlackTeam());
    }

    public List<RoomResponse> getGameList() {
        return chessRoomDao.getGames();
    }

    private Map<String, String> toBoardFormat() {
        Map<String, String> board = new HashMap<>();
        for (Position position : gameState.getBoard().keySet()) {
            Piece piece = gameState.getBoard().get(position);
            board.put(position.getPositionToString(), piece.getSymbol());
        }
        return board;
    }
}
