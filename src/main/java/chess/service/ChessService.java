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
import chess.dto.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChessService {
    private final BoardDao boardDao;
    private final RoomDao roomDao;
    private GameState gameState;

    public ChessService(BoardDao boardDao, RoomDao roomDao) {
        this.boardDao = boardDao;
        this.roomDao = roomDao;
        this.gameState = makeGame();
    }

    private GameState makeGame() {
        RoomDto room = roomDao.findById();

        if (room == null) {
            createGame();
            return createGameState(roomDao.findById().getId());
        }
        return initGameState(room);
    }

    private void createGame() {
        roomDao.makeGame(Team.WHITE);
    }

    private WhiteTurn createGameState(long roomId) {
        Map<Position, Piece> board = BoardInitialize.create();
        boardDao.savePieces(board, roomId);
        return new WhiteTurn(board);
    }

    private Playing initGameState(RoomDto room) {
        Map<Position, Piece> board = new HashMap<>();
        for (PieceDto pieceOfPieces : boardDao.findAllPiece(room.getId())) {
            board.put(Position.from(pieceOfPieces.getPosition()), PieceFactory.create(pieceOfPieces.getSymbol()));
        }

        Team status = room.getStatus();
        if (status.isWhiteTeam()) {
            return new WhiteTurn(board);
        }
        return new BlackTurn(board);
    }

    public GameStateDto move(MoveDto moveDTO, long roomId) {
        updateMove(roomId, moveDTO.getSource(), moveDTO.getDestination());

        GameState gameState = changeState(roomId);
        Team team = gameState.getTeam();
        return new GameStateDto(team, gameState.isRunning());
    }

    private void updateMove(long roomId, String source, String destination) {
        Map<Position, Piece> board = gameState.getBoard();
        Piece sourcePiece = board.get(Position.from(source));
        gameState = gameState.move(source, destination);

        boardDao.updatePosition(Blank.SYMBOL, source, roomId);
        boardDao.updatePosition(sourcePiece.getSymbol(), destination, roomId);
    }

    private GameState changeState(long roomId) {
        if (!gameState.isRunning()) {
            roomDao.deleteGame();
            return gameState;
        }
        roomDao.updateStatus(gameState.getTeam(), roomId);
        return gameState;
    }

    public void resetBoard() {
        roomDao.deleteGame();
        gameState = makeGame();
    }

    public void endGame() {
        gameState = new Finished(gameState.getTeam(), gameState.getBoard());
        roomDao.deleteGame();
    }

    public GameStateDto findWinner() {
        Score score = new Score(gameState.getBoard());
        Team winningTeam = score.getWinningTeam();
        return new GameStateDto(winningTeam, gameState.isRunning());
    }

    public BoardDto getBoard() {
        List<PieceDto> pieces = toBoardFormat().entrySet()
                .stream()
                .map(piece -> new PieceDto(piece.getKey(), piece.getValue()))
                .collect(Collectors.toUnmodifiableList());

        return new BoardDto(pieces, gameState.getTeam());
    }

    private Map<String, String> toBoardFormat() {
        Map<String, String> board = new HashMap<>();
        for (Position position : gameState.getBoard().keySet()) {
            Piece piece = gameState.getBoard().get(position);
            board.put(position.getPositionToString(), piece.getSymbol());
        }
        return board;
    }

    public ScoreDto getStatus() {
        Score score = new Score(gameState.getBoard());
        return new ScoreDto(score.getTotalScoreWhiteTeam(), score.getTotalScoreBlackTeam());
    }

    public RoomDto getGame() {
        return roomDao.findById();
    }
}
