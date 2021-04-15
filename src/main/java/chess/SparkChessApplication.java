package chess;

import chess.controller.ChessWebController;
import chess.dao.CustomConnectionPool;
import chess.dao.DBChessDao;
import chess.dao.DBMovementDao;
import chess.service.ChessService;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;

public class SparkChessApplication {
    public static void main(String[] args) {
        ChessService chessService = new ChessService(new DBChessDao(CustomConnectionPool.create()), new DBMovementDao(CustomConnectionPool.create()));
        ChessWebController chessWebController = new ChessWebController(chessService);
        chessWebController.run();
    }
}
