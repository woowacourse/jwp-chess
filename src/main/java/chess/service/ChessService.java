package chess.service;

import chess.dao.BoardDao;
import chess.dao.RoomDao;
import chess.domain.Score;
import chess.domain.Team;
import chess.domain.piece.Blank;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.position.Position;
import chess.domain.state.BlackTurn;
import chess.domain.state.BoardInitialize;
import chess.domain.state.GameState;
import chess.domain.state.Playing;
import chess.domain.state.WhiteTurn;
import chess.dto.BoardDto;
import chess.dto.GameStateDto;
import chess.dto.PieceDto;
import chess.dto.RoomDto;
import chess.dto.ScoreDto;
import chess.entity.Room;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    BoardDao boardDao;
    RoomDao roomDao;

    public ChessService(BoardDao boardDao, RoomDao roomDao) {
        this.boardDao = boardDao;
        this.roomDao = roomDao;
    }

    public void createRoom(RoomDto room) {
        Long roomId = roomDao.save(room.toRoom());
        boardDao.saveAll(BoardInitialize.create(), roomId);
    }

    public List<RoomDto> findRoomList() {
        List<Room> rooms = roomDao.findAll();
        return rooms.stream()
            .map(room -> new RoomDto(room.getId(), room.getTeam(), room.getTitle(),
                room.getPassword(), room.getStatus()))
            .collect(Collectors.toList());
    }

    public void deleteBy(Long roomId, String title) {
        roomDao.deleteBy(roomId, title);
    }

    public ScoreDto getScoreBy(Long roomId) {
        GameState gameState = getGameState(roomId);
        Score score = new Score(gameState.getBoard());
        return new ScoreDto(score.getTotalScoreWhiteTeam(), score.getTotalScoreBlackTeam());
    }

    private GameState getGameState(Long roomId) {
        Room room = roomDao.findById(roomId);
        Map<Position, Piece> board = getPiecesOfRoom(room);
        return createTurn(room, board);
    }

    private Map<Position, Piece> getPiecesOfRoom(Room room) {
        List<PieceDto> pieces = boardDao.findAll(room.getId());
        Map<Position, Piece> board = new HashMap<>();
        for (PieceDto pieceOfPieces : pieces) {
            Piece piece = PieceFactory.create(pieceOfPieces.getSymbol());
            Position position = Position.from(pieceOfPieces.getPosition());
            board.put(position, piece);
        }
        return board;
    }

    private Playing createTurn(Room room, Map<Position, Piece> board) {
        Team status = room.getTeam();
        if (status.isWhiteTeam()) {
            return new WhiteTurn(board);
        }
        return new BlackTurn(board);
    }

    public List<PieceDto> getPiecesBy(Long roomId) {
        return boardDao.findAll(roomId);
    }

    public BoardDto restartBy(Long roomId) {
        boardDao.delete(roomId);
        boardDao.saveAll(BoardInitialize.create(), roomId);
        roomDao.updateTeam(Team.WHITE, roomId);
        return new BoardDto(getPiecesBy(roomId), Team.WHITE);
    }

    public GameStateDto findGameStateBy(Long roomId) {
        Room room = roomDao.findById(roomId);
        return new GameStateDto(room.getTeam(), room.getStatus());
    }

    public void endBy(Long roomId) {
        roomDao.updateTeam(Team.NONE, roomId);
    }

    public GameStateDto move(Long roomId, String source, String destination) {
        GameState gameState = getGameState(roomId);
        gameState.move(source, destination);
        Piece sourcePiece = gameState.getPiece(Position.from(source));

        boardDao.updatePosition(Blank.SYMBOL, source, roomId);
        boardDao.updatePosition(sourcePiece.getSymbol(), destination, roomId);

        if (!gameState.isRunning()) {
            roomDao.updateStatus(roomId);
            return new GameStateDto(gameState.getTeam(), gameState.isRunning());
        }
        roomDao.updateTeam(gameState.getTeam(), roomId);
        return new GameStateDto(gameState.getTeam(), gameState.isRunning());
    }
}
