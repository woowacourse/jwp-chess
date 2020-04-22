package wooteco.chess.dao;

import wooteco.chess.dto.CommandDto;

import java.util.List;

public interface ChessDao {
    void addCommand(CommandDto command);

    void clearCommands();

    List<CommandDto> selectCommands();
}