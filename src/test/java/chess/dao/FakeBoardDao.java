package chess.dao;

import chess.dto.BoardDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeBoardDao implements BoardDao {

    private final Map<Integer, List<BoardDto>> map = new HashMap<>();

    @Override
    public List<BoardDto> findByGameId(int gameId) {
        return map.get(gameId);
    }

    @Override
    public void save(List<BoardDto> boardDtos, int gameId) {
        map.put(gameId, boardDtos);
    }

    @Override
    public void update(BoardDto boardDto, int gameId) {
        List<BoardDto> boardDtos = map.get(gameId);
        boardDtos.removeIf(bd -> bd.getPosition().equals(boardDto.getPosition()));
        boardDtos.add(boardDto);
    }

    public void deleteByGameId(int gameId) {
        map.remove(gameId);
    }
}
