package chess.dto;

import chess.dto.response.ChessGameStatusDto;
import java.util.ArrayList;
import java.util.List;

public class ChessGameInfoDto {

    private final int id;
    private final String name;
    private final String turn;
    private final boolean running;

    public ChessGameInfoDto(int id, String name, String turn, boolean running) {
        this.id = id;
        this.name = name;
        this.turn = turn;
        this.running = running;
    }

    public static List<ChessGameStatusDto> from(List<ChessGameInfoDto> games) {
        List<ChessGameStatusDto> gameStatuses = new ArrayList<>();
        for (ChessGameInfoDto game : games) {
            gameStatuses.add(from(game));
        }
        return gameStatuses;
    }

    public static ChessGameStatusDto from(ChessGameInfoDto game) {
        return new ChessGameStatusDto(game.getId(), game.getName(), game.getTurn(), game.running);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTurn() {
        return turn;
    }

    public boolean isRunning() {
        return running;
    }
}
