package wooteco.chess;

import wooteco.chess.controller.WebChessGameController;
import wooteco.chess.service.ChessService;

public class SparkChessApplication {
    public static void main(String[] args) {
        new WebChessGameController(new ChessService());
    }
}
