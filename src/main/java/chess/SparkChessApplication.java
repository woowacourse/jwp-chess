package chess;


import chess.controller.SparkController;
import spark.Spark;

import static spark.Spark.get;
import static spark.Spark.post;

public class SparkChessApplication {
    public static void main(String[] args) {
        Spark.staticFileLocation("public");

        SparkController sparkController = new SparkController();

        get("/", sparkController::mainPage);
        post("/start", sparkController::startGame);
        get("/create/:room", sparkController::createRoom);
        post("/move", sparkController::move);
        post("/movable", sparkController::movablePosition);
        post("/score", sparkController::score);
        get("/clear/:room", sparkController::clear);
    }
}
