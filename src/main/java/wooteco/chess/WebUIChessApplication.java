package wooteco.chess;

import static spark.Spark.*;

import wooteco.chess.controller.WebUIChessController;
import wooteco.chess.dao.BoardDao;
import wooteco.chess.dao.ConnectionManager;
import wooteco.chess.dao.JDBCTemplate;
import wooteco.chess.dao.RoomDao;
import wooteco.chess.service.ChessService;

public class WebUIChessApplication {
    public static void main(String[] args) {
        JDBCTemplate jdbcTemplate = new JDBCTemplate(new ConnectionManager());
        ChessService service = new ChessService(new BoardDao(jdbcTemplate), new RoomDao(jdbcTemplate));
        WebUIChessController controller = new WebUIChessController(service);

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
