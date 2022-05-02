package chess.dao;

import chess.service.dto.ChessGameDto;
import chess.service.dto.GamesDto;
import chess.service.dto.StatusDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryGameDao implements GameDao {

    private static final String DEFAULT_STATUS = "EMPTY";
    private static final String DEFAULT_TURN = "WHITE";
    private Map<Long, ChessGameDto> gameTable = new HashMap<>();
    private long id = 1;


    @Override
    public Long createGame(String name, String password) {
        gameTable.put(id, new ChessGameDto(id, name, DEFAULT_STATUS, DEFAULT_TURN, password));
        return id++;
    }

    @Override
    public void deleteGame(Long id) {
        gameTable.remove(id);
    }

    @Override
    public boolean existsById(Long gameId) {
        return gameTable.containsKey(gameId);
    }

    @Override
    public void removeAll() {
        gameTable = new HashMap<>();
    }

    @Override
    public void update(ChessGameDto dto) {
        ChessGameDto chessGameDto = gameTable.get(dto.getId());
        gameTable.put(dto.getId(), new ChessGameDto(chessGameDto.getId(), chessGameDto.getName(),
            dto.getStatus(), dto.getTurn(), chessGameDto.getPassword()));
    }

    @Override
    public ChessGameDto findById(Long id) {
        return gameTable.get(id);
    }

    @Override
    public void updateStatus(StatusDto statusDto, Long id) {
        String replaceStatus = statusDto.getStatus();
        ChessGameDto chessGameDto = gameTable.get(id);
        ChessGameDto replaceChessGameDto = new ChessGameDto(chessGameDto.getId(), replaceStatus,
                chessGameDto.getTurn());
        gameTable.put(1L, replaceChessGameDto);
    }

    @Override
    public GamesDto findAll() {
        return new GamesDto(new ArrayList<>(gameTable.values()));
    }

    public Map<Long, ChessGameDto> getGameTable() {
        return gameTable;
    }
}
