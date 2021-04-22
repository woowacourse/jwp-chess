package chess.dao;

import chess.dto.CommandDto;
import chess.dto.MoveRequestDto;

import java.util.List;

public interface ChessRepository {
    Long add(MoveRequestDto moveRequestDto);

    List<CommandDto> find(String roomId);

    void delete(String roomId);

    String findRoomById(String id);
}
