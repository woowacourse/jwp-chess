package wooteco.chess.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wooteco.chess.domain.Color;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Pieces;
import wooteco.chess.dto.RoomRequestDto;
import wooteco.chess.dto.RoomResponseDto;
import wooteco.chess.exception.WrongIdException;
import wooteco.chess.repository.RoomRepository;
import wooteco.chess.repository.entity.GameEntity;
import wooteco.chess.repository.entity.PieceEntity;
import wooteco.chess.repository.entity.RoomEntity;

@Service
public class SpringRoomService {

    @Autowired
    private RoomRepository roomRepository;

    public RoomEntity addRoom(RoomRequestDto roomRequestDto) throws SQLException {
        Set<PieceEntity> pieceEntities = convertPiecesToPieceEntity(Pieces.initPieces());
        GameEntity gameEntity = new GameEntity(Color.WHITE, pieceEntities);
        return roomRepository.save(
            new RoomEntity(roomRequestDto.getName(), roomRequestDto.getPassword(), gameEntity));
    }

    public void removeRoom(Long id) throws SQLException {
        roomRepository.deleteById(id);
    }

    public List<RoomResponseDto> findAllRoom() throws SQLException {
        return convertRoomEntityToDto(roomRepository.findAll());

    }

    private List<RoomResponseDto> convertRoomEntityToDto(final List<RoomEntity> roomEntities) {
        return roomEntities.stream()
            .map(roomEntity -> new RoomResponseDto(roomEntity.getId(), roomEntity.getName(),
                roomEntity.getPassword()))
            .collect(Collectors.toList());
    }

    private Set<PieceEntity> convertPiecesToPieceEntity(final Map<Position, Piece> pieces) {
        return pieces.entrySet().stream()
            .map(entry ->
                new PieceEntity(entry.getValue().getSymbol(),
                    entry.getValue().getColor().name(),
                    entry.getKey().getPosition()))
            .collect(Collectors.toSet());
    }

    public boolean authorize(final Long id, final String password) {
        RoomEntity roomEntity = roomRepository.findById(id)
            .orElseThrow(RuntimeException::new);

        return password.equals(roomEntity.getPassword());
    }

    public RoomEntity findById(Long id) {
        return roomRepository.findById(id).orElseThrow(WrongIdException::new);
    }
}
