package wooteco.chess;

import static spark.Spark.*;

import wooteco.chess.controller.WebUIChessController;
import wooteco.chess.dao.BoardDao;
import wooteco.chess.dao.RoomDao;
import wooteco.chess.service.ChessService;

public class WebUIChessApplication {
    private static ChessService service = new ChessService(new BoardDao(), new RoomDao());
    private static WebUIChessController controller = new WebUIChessController(service);

    public static void main(String[] args) {
        staticFileLocation("/public");

        get("/new", controller.getNewChessGameRoute());
        get("/", controller.getChessGameRoute());
        get("/result", controller.getResultRoute());
        post("/make-room", controller.initChessGameRoute());
        post("/continue-game", controller.findChessGameRoute());
        post("/move", controller.postMoveRoute());
        post("/initialize", controller.postInitializeRoute());
    }
}
