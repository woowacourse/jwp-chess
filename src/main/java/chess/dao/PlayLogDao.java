package chess.dao;

import chess.dto.web.BoardDto;
import chess.dto.web.GameStatusDto;

public interface PlayLogDao {

    void insert(BoardDto boardDto, GameStatusDto gameStatusDto, String roomId);

    BoardDto latestBoard(String roomId);

    GameStatusDto latestGameStatus(String roomId);
}
