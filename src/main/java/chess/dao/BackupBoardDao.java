package chess.dao;

import chess.domain.board.Board;
import chess.dto.SquareDto;

import java.util.List;

public interface BackupBoardDao {

    void deleteExistingBoard(String name);

    void addPlayingBoard(String name, Board board);

    List<SquareDto> findPlayingBoardByRoom(String name);
}
