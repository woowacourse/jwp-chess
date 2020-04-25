package wooteco.chess.dao;

import wooteco.chess.dto.Commands;

import java.util.List;

public interface ChessDao {
    void addCommand(Commands command);

    void clearCommands();

    List<Commands> selectCommands();
}