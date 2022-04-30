package chess.dao;

import chess.controller.dto.ChessRequestDto;
import chess.service.dto.GameDto;
import chess.service.dto.GameStatusDto;
import chess.service.dto.RoomResponseDto;

import java.util.List;

public interface GameDao {

    void removeAll(int id);

    void save(int id, GameDto gameDto);

    void save(int id, ChessRequestDto chessRequestDto);

    void modify(int id, GameDto gameDto);

    void modifyStatus(int id, GameStatusDto statusDto);

    GameDto find(int id);

    List<RoomResponseDto> findAll();

    Integer findLastGameId();
}
