package wooteco.chess;

import wooteco.chess.controller.WebChessGameRestController;
import wooteco.chess.service.ChessService;

public class SparkChessApplication {
    public static void main(String[] args) {
        new WebChessGameRestController(new ChessService());
    }
}
