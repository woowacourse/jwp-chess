package wooteco.chess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wooteco.chess.dto.RoomResponseDto;
import wooteco.chess.repository.GameDAO;
import wooteco.chess.domain.Color;
import wooteco.chess.domain.GameManager;
import wooteco.chess.domain.PieceScore;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Pieces;
import wooteco.chess.dto.MoveRequestDto;
import wooteco.chess.dto.MoveResponseDto;
import wooteco.chess.dto.PiecesResponseDto;
import wooteco.chess.repository.RoomRepository;
import wooteco.chess.repository.entity.RoomEntity;

import java.sql.SQLException;
import java.util.List;

@Service
public class SpringGameService {

    @Autowired
    private GameDAO gameDAO;

    @Autowired
    private RoomRepository roomRepository;

    public MoveResponseDto initialize(int roomId) throws SQLException {
        roomRepository.updateRoomColorById(roomId, Color.WHITE.name());
        Pieces pieces = new Pieces(Pieces.initPieces());
        gameDAO.addAllPiecesById(roomId, pieces);
        return new MoveResponseDto(new PiecesResponseDto(pieces).getPieces(), Color.WHITE, false);
    }

    public MoveResponseDto move(MoveRequestDto requestDTO) throws SQLException {
        Integer roomId = requestDTO.getRoomId();
        String sourcePosition = requestDTO.getSourcePosition();
        String targetPosition = requestDTO.getTargetPosition();

        Pieces pieces = new Pieces(gameDAO.findAllPiecesById(roomId));
        RoomResponseDto responseDto = roomRepository.findById(roomId)
                .map(RoomResponseDto::of)
                .orElseThrow(RuntimeException::new);
        GameManager gameManager = new GameManager(pieces, Color.valueOf(responseDto.getCurrentColor()));
        gameManager.validateEndGame();
        gameManager.moveFromTo(Position.of(sourcePosition), Position.of(targetPosition));
        roomRepository.updateRoomColorById(roomId, gameManager.getCurrentColor().name());
        gameDAO.removeAllPiecesById(roomId);
        gameDAO.addAllPiecesById(roomId, pieces);
        return new MoveResponseDto(new PiecesResponseDto(pieces).getPieces(), Color.valueOf(responseDto.getCurrentColor()),
                gameManager.isKingDead());
    }

    public double getScore(int roomId, Color color) throws SQLException {
        Pieces pieces = new Pieces(gameDAO.findAllPiecesById(roomId));
        GameManager gameManager = new GameManager(pieces, color);

        return PieceScore.calculateByColor(gameManager, color);
    }

    public PiecesResponseDto getPiecesResponseDTO(int roomId) throws SQLException {
        Pieces pieces = new Pieces(gameDAO.findAllPiecesById(roomId));
        return new PiecesResponseDto(pieces);
    }

    private boolean isKingDead(final int roomId) throws SQLException {
        Pieces pieces = new Pieces(gameDAO.findAllPiecesById(roomId));
        RoomResponseDto responseDto = roomRepository.findById(roomId)
                .map(RoomResponseDto::of)
                .orElseThrow(RuntimeException::new);
        Color currentColor = Color.valueOf(responseDto.getCurrentColor());

        return pieces.isKingDead(currentColor);
    }

    private Color getCurrentColor(final int roomId) throws SQLException {
        RoomResponseDto responseDto = roomRepository.findById(roomId)
                .map(RoomResponseDto::of)
                .orElseThrow(RuntimeException::new);
        return Color.valueOf(responseDto.getCurrentColor());
    }

    public List<String> getMovablePositions(final int roomId, final String sourcePosition) throws SQLException {
        Pieces pieces = new Pieces(gameDAO.findAllPiecesById(roomId));
        RoomResponseDto responseDto = roomRepository.findById(roomId)
                .map(RoomResponseDto::of)
                .orElseThrow(RuntimeException::new);
        Color currentColor = Color.valueOf(responseDto.getCurrentColor());

        GameManager gameManager = new GameManager(pieces, currentColor);

        return gameManager.getMovablePositions(Position.of(sourcePosition));
    }

    public MoveResponseDto createMoveResponseDTO(final int roomId) throws SQLException {
        return new MoveResponseDto(getPiecesResponseDTO(roomId).getPieces(),
                getCurrentColor(roomId), isKingDead(roomId));
    }
}
