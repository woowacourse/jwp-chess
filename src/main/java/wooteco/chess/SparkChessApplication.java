package wooteco.chess;

import wooteco.chess.controller.ChessController;

import static spark.Spark.*;

public class SparkChessApplication {
    public static void main(String[] args) {
        final ChessController controller = new ChessController();

        staticFiles.location("/");

        get("/", (req, res) -> controller.chessGame());

        post("/ready", (req, res) -> controller.enterGameRoom(req));

        post("/play", (req, res) -> controller.startGame(req));

        post("/resume", (req, res) -> controller.resumeGame(req));

        post("/move", (req, res) -> controller.move(req));
    }
}
