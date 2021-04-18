package chess;

import chess.controller.SparkChessController;
import chess.dao.CommandDao;
import chess.dao.RoomDao;
import chess.service.ChessService;
import chess.service.RoomService;

public class SparkChessApplication {
    public static void main(String[] args) {
        CommandDao commandDao = new CommandDao();
        RoomDao roomDao = new RoomDao();

        SparkChessController chessController =
                new SparkChessController(new ChessService(commandDao), new RoomService(roomDao));
        chessController.run();
    }
}
