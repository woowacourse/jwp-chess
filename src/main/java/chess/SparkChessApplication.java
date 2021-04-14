package chess;

import chess.service.*;
import chess.web.WebChessController;

import static spark.Spark.staticFiles;

public class SparkChessApplication {

    public static void main(String[] args) {
        staticFiles.location("/templates");

        WebChessController chessController = new WebChessController(
                new StartService(),
                new EndService(),
                new MoveService(),
                new SaveService(),
                new StatusService(),
                new LoadService()
        );
        chessController.run();
    }

}
