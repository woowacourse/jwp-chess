package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.dao.GameDAO;
import wooteco.chess.dao.RoomDAO;
import wooteco.chess.domain.Color;
import wooteco.chess.domain.GameManager;
import wooteco.chess.domain.PieceScore;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Pieces;
import wooteco.chess.dto.GameManagerDTO;
import wooteco.chess.dto.PiecesResponseDTO;

import java.sql.SQLException;
import java.util.List;

@Service
public class GameService {
    private static final GameService GAME_SERVICE = new GameService();

    private GameService() {
    }

    public static GameService getInstance() {
        return GAME_SERVICE;
    }

    public GameManagerDTO initialize(int roomId) throws SQLException {
        GameDAO gameDAO = GameDAO.getInstance();
        RoomDAO roomDAO = RoomDAO.getInstance();

        roomDAO.updateRoomColorById(roomId, Color.WHITE);
        Pieces pieces = new Pieces(Pieces.initPieces());
        gameDAO.addAllPiecesById(roomId, pieces);
        return new GameManagerDTO(new PiecesResponseDTO(pieces).getPieces(), Color.WHITE, false);
    }

    public void movePiece(int roomId, String sourcePosition, String targetPosition) throws SQLException {
        GameDAO gameDAO = GameDAO.getInstance();
        RoomDAO roomDAO = RoomDAO.getInstance();

        Pieces pieces = new Pieces(gameDAO.findAllPiecesById(roomId));
        GameManager gameManager = new GameManager(pieces, roomDAO.findRoomColorById(roomId));
        gameManager.validateEndGame();
        gameManager.moveFromTo(Position.of(sourcePosition), Position.of(targetPosition));
        roomDAO.updateRoomColorById(roomId, gameManager.getCurrentColor());

        gameDAO.removeAllPiecesById(roomId);
        gameDAO.addAllPiecesById(roomId, pieces);
    }

    public double getScore(int roomId, Color color) throws SQLException {
        GameDAO gameDAO = GameDAO.getInstance();
        Pieces pieces = new Pieces(gameDAO.findAllPiecesById(roomId));
        GameManager gameManager = new GameManager(pieces, color);

        return PieceScore.calculateByColor(gameManager, color);
    }

    public PiecesResponseDTO getPiecesResponseDTO(int roomId) throws SQLException {
        GameDAO gameDAO = GameDAO.getInstance();
        Pieces pieces = new Pieces(gameDAO.findAllPiecesById(roomId));
        return new PiecesResponseDTO(pieces);
    }

    public boolean isKingDead(final int roomId) throws SQLException {
        GameDAO gameDAO = GameDAO.getInstance();
        RoomDAO roomDAO = RoomDAO.getInstance();

        Pieces pieces = new Pieces(gameDAO.findAllPiecesById(roomId));
        GameManager gameManager = new GameManager(pieces, roomDAO.findRoomColorById(roomId));
        return gameManager.isKingDead();
    }

    public String getCurrentColor(final int roomId) throws SQLException {
        RoomDAO roomDAO = RoomDAO.getInstance();
        return roomDAO.findRoomColorById(roomId).name();
    }

    public List<String> getMovablePositions(final int roomId, final String sourcePosition) throws SQLException {
        GameDAO gameDAO = GameDAO.getInstance();
        RoomDAO roomDAO = RoomDAO.getInstance();

        Pieces pieces = new Pieces(gameDAO.findAllPiecesById(roomId));
        GameManager gameManager = new GameManager(pieces, roomDAO.findRoomColorById(roomId));

        return gameManager.getMovablePositions(Position.of(sourcePosition));
    }
}
