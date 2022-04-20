package springchess.controller;

import springchess.service.ChessService;

public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }
}
