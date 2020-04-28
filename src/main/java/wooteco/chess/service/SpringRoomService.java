package wooteco.chess.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wooteco.chess.domain.Color;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Pieces;
import wooteco.chess.dto.RoomDto;
import wooteco.chess.repository.RoomRepository;
import wooteco.chess.repository.entity.GameEntity;
import wooteco.chess.repository.entity.PieceEntity;
import wooteco.chess.repository.entity.RoomEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SpringRoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ModelMapper modelMapper;

    public void addRoom(RoomDto roomDto) throws SQLException {
        Set<PieceEntity> pieceEntities = convertPiecesToPieceEntity(Pieces.initPieces());
        GameEntity gameEntity = new GameEntity(Color.WHITE, pieceEntities);
        roomRepository.save(new RoomEntity(roomDto.getRoomName(), roomDto.getPassword(), gameEntity));
    }

    public void removeRoom(RoomDto roomDto) throws SQLException {
        roomRepository.deleteById(roomDto.getId());
    }

    public List<RoomDto> findAllRoom() throws SQLException {
        return convertRoomEntityToDto(roomRepository.findAll());

    }

    private List<RoomDto> convertRoomEntityToDto(final List<RoomEntity> roomEntities) {
        return roomEntities.stream()
                .map(roomEntity -> new RoomDto(roomEntity.getId(), roomEntity.getName(), roomEntity.getPassword()))
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
}
