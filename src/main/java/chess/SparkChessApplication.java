package chess;

import chess.controller.SparkChessGameController;
import chess.dao.LogDAO;
import chess.dao.ResultDAO;
import chess.dao.RoomDAO;
import chess.dao.UserDAO;
import chess.domain.Rooms;
import chess.service.LogService;
import chess.service.ResultService;
import chess.service.RoomService;
import chess.service.UserService;

import static spark.Spark.staticFiles;

public class SparkChessApplication {
    public static void main(String[] args) {
        staticFiles.location("/static");

        RoomService roomService = new RoomService(new RoomDAO());
        UserDAO userDAO = new UserDAO();
        SparkChessGameController sparkChessGameController = new SparkChessGameController(
                roomService,
                new ResultService(new ResultDAO(), userDAO),
                new UserService(userDAO),
                new LogService(new LogDAO())
        );

        sparkChessGameController.start(new Rooms());
    }
}
