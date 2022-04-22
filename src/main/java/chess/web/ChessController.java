package chess.web;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import chess.web.dao.BoardDao;
import chess.web.dao.PieceDao;
import chess.web.dao.RoomDao;
import chess.web.dto.BoardDto;
import chess.web.dto.CommendDto;
import chess.web.dto.RoomDto;
import chess.web.service.GameService;
import chess.web.service.RoomService;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;


public class ChessController {

    private final Gson gson = new Gson();
    private final GameService gameService = new GameService(new PieceDao(), new BoardDao());
    private final RoomService roomService = new RoomService(new RoomDao());

    public ChessController() {
        port(8080);
        Spark.staticFileLocation("/static");
    }

    public void run() {
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "login.html");
        });

        get("/board", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "index.html");
        });

        post("/board", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            RoomDto roomDto;
            try {
                roomDto = roomService.create(req.queryParams("name"));
            } catch (IllegalArgumentException e) {
                res.status(400);
                model.put("message", e.getMessage());
                return render(model, "login.html");
            }
            res.redirect("/board?roomId=" + roomDto.getId());
            return null;
        });

        get("/start", (req, res) ->
                gson.toJson(gameService.startNewGame(Integer.parseInt(req.queryParams("roomId"))))
        );

        get("/load", (req, res) ->
                gson.toJson(gameService.loadGame(Integer.parseInt(req.queryParams("roomId"))))
        );

        post("/move", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int boardId = Integer.parseInt(req.queryParams("boardId"));
            CommendDto commendDto = gson.fromJson(req.body(), CommendDto.class);
            try {
                gameService.move(boardId, commendDto);
            } catch (Exception e) {
                res.status(400);
                model.put("message", e.getMessage());
            }
            BoardDto boardDto = gameService.gameStateAndPieces(boardId);
            model.put("boardId", boardDto.getBoardId());
            model.put("state", boardDto.getState());
            model.put("pieces", boardDto.getPieces());
            return gson.toJson(model);
        });

        get("/result", (req, res) ->
                gson.toJson(gameService.gameResult(Integer.parseInt(req.queryParams("boardId"))))
        );

        get("/end", (req, res) ->
                gson.toJson(gameService.gameFinalResult(Integer.parseInt(req.queryParams("boardId"))))
        );
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }

}
