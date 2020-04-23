package wooteco.chess;

import wooteco.chess.controller.ChessRestController;
import wooteco.chess.service.ChessService;

public class SparkChessApplication {
    public static void main(String[] args) {
        new ChessRestController(new ChessService());
    }
}
