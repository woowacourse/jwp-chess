package chess.service;

import chess.dao.*;
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
    BoardDao boardDao;
    RoomDao roomDao;

    public ChessService(BoardDao boardDao, RoomDao roomDao) {
        this.boardDao = boardDao;
        this.roomDao = roomDao;
    }

    public BoardDto getInitialBoard(long roomId) {
        GameState gameState = getGameState(roomId);
        Map<String, String> board = getBoardPieces(roomId);
        List<PieceDto> pieces = board.entrySet()
                .stream()
                .map(i -> new PieceDto(i.getKey(), i.getValue()))
                .collect(Collectors.toUnmodifiableList());
        return new BoardDto(pieces, gameState.getTeam());
    }

    private GameState getGameState(long roomId) {
        RoomDto room = roomDao.findById(roomId);
        if (room == null) {
            return createGameState(roomId);
        }
        return getGameState(room);
    }

    private WhiteTurn createGameState(long roomId) {
        roomDao.save(roomId, Team.WHITE);
        Map<Position, Piece> board = BoardInitialize.create();
        boardDao.saveAll(board, roomId);
        return new WhiteTurn(board);
    }

    private Playing getGameState(RoomDto room) {
        List<PieceDto> pieces = boardDao.findAll(room.getId());
        Map<Position, Piece> board = new HashMap<>();
        for (PieceDto pieceOfPieces : pieces) {
            Piece piece = PieceFactory.create(pieceOfPieces.getSymbol());
            Position position = Position.from(pieceOfPieces.getPosition());
            board.put(position, piece);
        }
        Team status = room.getStatus();
        if (status.isWhiteTeam()) {
            return new WhiteTurn(board);
        }
        return new BlackTurn(board);
    }

    public GameStateDto move(MoveDto moveDTO, long roomId) {
        String source = moveDTO.getSource();
        String destination = moveDTO.getDestination();

        GameState gameState = updateMove(roomId, source, destination);

        Team team = gameState.getTeam();
        return new GameStateDto(team, gameState.isRunning());
    }

    private GameState updateMove(long roomId, String source, String destination) {
        GameState gameState = getGameState(roomId);
        Map<Position, Piece> board = gameState.getBoard();
        Piece sourcePiece = board.get(Position.from(source));
        gameState = gameState.move(source, destination);

        boardDao.updatePosition(Blank.SYMBOL, source, roomId);
        boardDao.updatePosition(sourcePiece.getSymbol(), destination, roomId);

        if (!gameState.isRunning()) {
            deleteAll(roomId);
            return gameState;
        }
        roomDao.updateStatus(gameState.getTeam(), roomId);
        return gameState;
    }

    public ScoreDto getStatus(long roomId) {
        GameState gameState = getGameState(roomId);
        Score score = new Score(gameState.getBoard());
        return new ScoreDto(score.getTotalScoreWhiteTeam(), score.getTotalScoreBlackTeam());
    }

    public BoardDto resetBoard(long roomId) {
        deleteAll(roomId);
        GameState gameState = createGameState(roomId);
        Map<String, String> board = getBoardPieces(roomId);
        List<PieceDto> pieces = board.entrySet()
                .stream()
                .map(i -> new PieceDto(i.getKey(), i.getValue()))
                .collect(Collectors.toUnmodifiableList());
        return new BoardDto(pieces, gameState.getTeam());
    }

    private Map<String, String> getBoardPieces(long roomId) {
        GameState gameState = getGameState(roomId);
        Map<Position, Piece> chessBoard = gameState.getBoard();
        Map<String, String> board = new HashMap<>();
        for (Position position : chessBoard.keySet()) {
            Piece piece = chessBoard.get(position);
            board.put(position.getPositionToString(), piece.getSymbol());
        }
        return board;
    }

    private void deleteAll(long roomId) {
        roomDao.delete(roomId);
        boardDao.delete(roomId);
    }

    public GameStateDto end(long roomId) {
        GameState gameState = getGameState(roomId);
        gameState = new Finished(gameState.getTeam(), gameState.getBoard());
        deleteAll(roomId);
        Score score = new Score(gameState.getBoard());
        Team winningTeam = score.getWinningTeam();
        return new GameStateDto(winningTeam, gameState.isRunning());
    }
}
