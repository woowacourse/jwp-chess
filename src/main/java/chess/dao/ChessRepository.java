package chess.dao;

import chess.dto.CommandDto;
import chess.dto.MoveRequestDto;

import java.util.List;
import java.util.Optional;

public interface ChessRepository {
    Long add(MoveRequestDto moveRequestDto);

    List<CommandDto> find(String roomId);

    void delete(String roomId);

    Optional<String> findRoomByName(String roomId);
}
