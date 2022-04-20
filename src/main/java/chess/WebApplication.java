package chess;

import chess.controller.WebPathController;

import static spark.Spark.*;

public class WebApplication {
    public static void main(String[] args) {
        staticFiles.location("/static");
        port(8081);

        WebPathController webPathController = new WebPathController();

        get("/", webPathController.ready());
        get("/findgame", webPathController.askGameID());
        post("/findgame", webPathController.findGame());
        get("/ingame", webPathController.runGame());
        post("/ingame", webPathController.movePiece());
        get("/status", webPathController.showResult());
    }
}
