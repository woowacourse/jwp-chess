package wooteco.chess.service;

import wooteco.chess.dao.SparkGameDAO;
import wooteco.chess.dao.SparkRoomDAO;
import wooteco.chess.domain.Color;
import wooteco.chess.domain.GameManager;
import wooteco.chess.domain.PieceScore;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Pieces;
import wooteco.chess.dto.PiecesResponseDTO;

import java.sql.SQLException;
import java.util.List;

public class SparkGameService {
    private static final SparkGameService GAME_SERVICE = new SparkGameService();

    private SparkGameService() {
    }

    public static SparkGameService getInstance() {
        return GAME_SERVICE;
    }

    public void initialize(int roomId) throws SQLException {
        SparkGameDAO gameDAO = SparkGameDAO.getInstance();
        SparkRoomDAO roomDAO = SparkRoomDAO.getInstance();

        roomDAO.updateRoomColorById(roomId, Color.WHITE);
        Pieces pieces = new Pieces(Pieces.initPieces());
        gameDAO.addAllPiecesById(roomId, pieces);
    }

    public void movePiece(int roomId, String sourcePosition, String targetPosition) throws SQLException {
        SparkGameDAO gameDAO = SparkGameDAO.getInstance();
        SparkRoomDAO roomDAO = SparkRoomDAO.getInstance();

        Pieces pieces = new Pieces(gameDAO.findAllPiecesById(roomId));
        GameManager gameManager = new GameManager(pieces, roomDAO.findRoomColorById(roomId));
        gameManager.moveFromTo(Position.of(sourcePosition), Position.of(targetPosition));
        roomDAO.updateRoomColorById(roomId, gameManager.getCurrentColor());

        gameDAO.removeAllPiecesById(roomId);
        gameDAO.addAllPiecesById(roomId, pieces);
    }

    public double getScore(int roomId, Color color) throws SQLException {
        SparkGameDAO gameDAO = SparkGameDAO.getInstance();
        Pieces pieces = new Pieces(gameDAO.findAllPiecesById(roomId));
        GameManager gameManager = new GameManager(pieces, color);

        return PieceScore.calculateByColor(gameManager, color);
    }

    public PiecesResponseDTO getPiecesResponseDTO(int roomId) throws SQLException {
        SparkGameDAO gameDAO = SparkGameDAO.getInstance();
        Pieces pieces = new Pieces(gameDAO.findAllPiecesById(roomId));
        return new PiecesResponseDTO(pieces);
    }

    public boolean isKingDead(final int roomId) throws SQLException {
        SparkGameDAO gameDAO = SparkGameDAO.getInstance();
        SparkRoomDAO roomDAO = SparkRoomDAO.getInstance();

        Pieces pieces = new Pieces(gameDAO.findAllPiecesById(roomId));
        GameManager gameManager = new GameManager(pieces, roomDAO.findRoomColorById(roomId));
        return gameManager.isKingDead();
    }

    public String getCurrentColor(final int roomId) throws SQLException {
        SparkRoomDAO roomDAO = SparkRoomDAO.getInstance();
        return roomDAO.findRoomColorById(roomId).name();
    }

    public List<String> getMovablePositions(final int roomId, final String sourcePosition) throws SQLException {
        SparkGameDAO gameDAO = SparkGameDAO.getInstance();
        SparkRoomDAO roomDAO = SparkRoomDAO.getInstance();

        Pieces pieces = new Pieces(gameDAO.findAllPiecesById(roomId));
        GameManager gameManager = new GameManager(pieces, roomDAO.findRoomColorById(roomId));

        return gameManager.getMovablePositions(Position.of(sourcePosition));
    }
}
