package wooteco.chess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wooteco.chess.dao.GameDAO;
import wooteco.chess.dao.RoomDAO;
import wooteco.chess.domain.Color;
import wooteco.chess.domain.GameManager;
import wooteco.chess.domain.PieceScore;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Pieces;
import wooteco.chess.dto.MoveRequestDTO;
import wooteco.chess.dto.MoveResponseDTO;
import wooteco.chess.dto.PiecesResponseDTO;

import java.sql.SQLException;
import java.util.List;

@Service
public class SpringGameService {

    @Autowired
    private GameDAO gameDAO;

    @Autowired
    private RoomDAO roomDAO;

    public MoveResponseDTO initialize(int roomId) throws SQLException {
        roomDAO.updateRoomColorById(roomId, Color.WHITE);
        Pieces pieces = new Pieces(Pieces.initPieces());
        gameDAO.addAllPiecesById(roomId, pieces);
        return new MoveResponseDTO(new PiecesResponseDTO(pieces).getPieces(), Color.WHITE, false);
    }

    public MoveResponseDTO move(MoveRequestDTO requestDTO) throws SQLException {
        Integer roomId = requestDTO.getRoomId();
        String sourcePosition = requestDTO.getSourcePosition();
        String targetPosition = requestDTO.getTargetPosition();

        Pieces pieces = new Pieces(gameDAO.findAllPiecesById(roomId));
        Color currentColor = roomDAO.findRoomColorById(roomId);
        GameManager gameManager = new GameManager(pieces, currentColor);
        gameManager.validateEndGame();
        gameManager.moveFromTo(Position.of(sourcePosition), Position.of(targetPosition));
        roomDAO.updateRoomColorById(roomId, gameManager.getCurrentColor());
        gameDAO.removeAllPiecesById(roomId);
        gameDAO.addAllPiecesById(roomId, pieces);
        return new MoveResponseDTO(new PiecesResponseDTO(pieces).getPieces(), roomDAO.findRoomColorById(roomId),
                gameManager.isKingDead());
    }

    public double getScore(int roomId, Color color) throws SQLException {
        Pieces pieces = new Pieces(gameDAO.findAllPiecesById(roomId));
        GameManager gameManager = new GameManager(pieces, color);

        return PieceScore.calculateByColor(gameManager, color);
    }

    public PiecesResponseDTO getPiecesResponseDTO(int roomId) throws SQLException {
        Pieces pieces = new Pieces(gameDAO.findAllPiecesById(roomId));
        return new PiecesResponseDTO(pieces);
    }

    private boolean isKingDead(final int roomId) throws SQLException {
        Pieces pieces = new Pieces(gameDAO.findAllPiecesById(roomId));
        Color currentColor = roomDAO.findRoomColorById(roomId);
        return pieces.isKingDead(currentColor);
    }

    private Color getCurrentColor(final int roomId) throws SQLException {
        return roomDAO.findRoomColorById(roomId);
    }

    public List<String> getMovablePositions(final int roomId, final String sourcePosition) throws SQLException {
        Pieces pieces = new Pieces(gameDAO.findAllPiecesById(roomId));
        GameManager gameManager = new GameManager(pieces, roomDAO.findRoomColorById(roomId));

        return gameManager.getMovablePositions(Position.of(sourcePosition));
    }

    public MoveResponseDTO createMoveResponseDTO(final int roomId) throws SQLException {
        return new MoveResponseDTO(getPiecesResponseDTO(roomId).getPieces(),
                getCurrentColor(roomId), isKingDead(roomId));
    }
}
