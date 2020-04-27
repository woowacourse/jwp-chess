package wooteco.chess.controller;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import wooteco.chess.database.dao.ChessDao;
import wooteco.chess.domain.Scores;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.service.BoardService;
import wooteco.chess.service.RoomService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class SparkChessController {
    public static void main(String[] args) {
        staticFiles.location("/public");
        BoardService boardService = new BoardService(new ChessDao());
        RoomService roomService = new RoomService();

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("roomNumbers", roomService.loadRoomNumbers());

            return render(model, "index.hbs");
        });

        post("/newroom", (req, res) -> {
            int roomId = roomService.create();
            res.redirect("/rooms/" + roomId);
            return null;
        });

        get("/rooms/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Map<String, Object> model = new HashMap<>();
            constructModel(id, boardService, model);
            return render(model, "board.hbs");
        });

        post("/rooms/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Map<String, Object> model = new HashMap<>();
            String source = req.queryParams("source");
            String target = req.queryParams("target");
            try {
                boardService.play(id, source, target);
            } catch (RuntimeException e) {
                model.put("error-message", e.getMessage());
                constructModel(id, boardService, model);
                return render(model, "board.hbs");
            }
            if (boardService.isFinished(id)) {
                model.put("winner", boardService.isTurnWhite(id) ? "흑팀" : "백팀");
                return render(model, "result.hbs");
            }
            constructModel(id, boardService, model);
            return render(model, "board.hbs");
        });

        post("/newgame/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            boardService.init(id);
            res.redirect("/rooms/" + id);
            return null;
        });

        get("/scores/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Map<String, Object> model = new HashMap<>();
            model.put("id", id);
            model.put("scores", Scores.calculateScores(boardService.getBoard(id)));
            return render(model, "scores.hbs");
        });
    }

    private static void constructModel(int roomId, BoardService service, Map<String, Object> model) throws SQLException {
        BoardDto boardDTO = new BoardDto(service.getBoard(roomId));
        Map<String, String> pieces = boardDTO.getBoard();
        for (String positionKey : pieces.keySet()) {
            String imageName = pieces.get(positionKey);
            if (imageName.equals(".")) {
                imageName = "blank";
            }
            model.put(positionKey, imageName);
            model.put("id", roomId);
        }
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}