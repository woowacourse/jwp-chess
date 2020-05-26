package spring.dto;

import spring.entity.ChessGameEntity;
import spring.vo.ChessGameVo;

import java.util.ArrayList;
import java.util.List;

public class ChessGamesDto {
    private final List<ChessGameVo> games;

    public ChessGamesDto(List<ChessGameEntity> chessGameEntities) {
        this.games = new ArrayList<>();
        chessGameEntities.forEach(chessGameEntity -> this.games.add(new ChessGameVo(chessGameEntity)));
    }

    public List<ChessGameVo> getGames() {
        return games;
    }
}
