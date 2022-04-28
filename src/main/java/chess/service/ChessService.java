package chess.service;

import chess.dao.BoardDao;
import chess.dao.ChessBoardDao;
import chess.dao.ChessRoomDao;
import chess.dao.RoomDao;
import chess.domain.Score;
import chess.domain.Team;
import chess.domain.piece.Blank;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.position.Position;
import chess.domain.state.*;
import chess.dto.*;
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

    public Long makeGame(MakeRoomDto makeRoomDto) {
        RoomStatusDto room = chessRoomDao.findById(makeRoomDto);
        if (room == null) {
            createGame(makeRoomDto);
            gameState = createGameState(chessRoomDao.findById(makeRoomDto).getId());
            room = chessRoomDao.findById(makeRoomDto);
        }
        gameState = initGameState(room);
        return chessRoomDao.findById(makeRoomDto).getId();
    }

    private void createGame(MakeRoomDto makeRoomDto) {
        chessRoomDao.makeGame(Team.WHITE, makeRoomDto);
    }

    private WhiteTurn createGameState(long roomId) {
        Map<Position, Piece> board = BoardInitialize.create();
        chessBoardDao.savePieces(board, roomId);
        return new WhiteTurn(board);
    }

    private Playing initGameState(RoomStatusDto room) {
        Map<Position, Piece> board = new HashMap<>();
        for (PieceDto pieceOfPieces : chessBoardDao.findAllPiece(room.getId())) {
            board.put(Position.from(pieceOfPieces.getPosition()), PieceFactory.create(pieceOfPieces.getSymbol()));
        }

        Team status = room.getStatus();
        if (status.isWhiteTeam()) {
            return new WhiteTurn(board);
        }
        return new BlackTurn(board);
    }

    public GameStateDto move(Long id, MoveDto moveDTO) {
        updateMove(id, moveDTO.getSource(), moveDTO.getDestination());
        GameState gameState = changeState(id);
        Team team = gameState.getTeam();
        return new GameStateDto(team, gameState.isRunning());
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

    public void resetBoard(RoomDto room, long id) {
        chessRoomDao.deleteGame(id);
        MakeRoomDto newRoom = new MakeRoomDto(room.getName(), room.getPassword());
        createGame(newRoom);
        gameState = createGameState(chessRoomDao.findById(newRoom).getId());
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
        RoomDto room = chessRoomDao.findById(new GameIdDto(id));
        return (room.getStatus().equals(Team.NONE)) && (room.getPassword().equals(password));
    }

    public GameStateDto findWinner() {
        Score score = new Score(gameState.getBoard());
        Team winningTeam = score.getWinningTeam();
        return new GameStateDto(winningTeam, gameState.isRunning());
    }

    public RoomDto findRoom(long id) {
        return chessRoomDao.findById(new GameIdDto(id));
    }

    public BoardDto getBoard(long id) {
        List<PieceDto> pieces = toBoardFormat().entrySet()
                .stream()
                .map(piece -> new PieceDto(piece.getKey(), piece.getValue()))
                .collect(Collectors.toUnmodifiableList());

        return new BoardDto(pieces, gameState.getTeam(), id);
    }

    public ScoreDto getStatus() {
        Score score = new Score(gameState.getBoard());
        return new ScoreDto(score.getTotalScoreWhiteTeam(), score.getTotalScoreBlackTeam());
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
