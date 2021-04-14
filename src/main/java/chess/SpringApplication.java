package chess;

import chess.controller.SpringChessController;
import chess.repository.ChessRepositoryImpl;
import chess.service.ChessServiceImpl;

import static spark.Spark.staticFileLocation;

public class SpringApplication {
    public static void main(String[] args) {
        staticFileLocation("/static");
        SpringChessController springChessController = new SpringChessController(
                new ChessServiceImpl(new ChessRepositoryImpl()));
        springChessController.run();
    }
}
