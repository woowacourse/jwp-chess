package spark.dto;

import java.util.ArrayList;

public class ChessGamesDto {
    private final ArrayList<ChessGameVo> chessGameVos;

    public ChessGamesDto(ArrayList<ChessGameVo> chessGameVos) {
        this.chessGameVos = chessGameVos;
    }
}
