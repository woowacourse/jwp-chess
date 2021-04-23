package chess;

import chess.dto.PositionDto;
import chess.service.ChessService;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class SparkChessApplication {
    public static void main(String[] args) {
        JsonTransformer jsonTransformer = new JsonTransformer();

        ChessService chessService = new ChessService();
        staticFiles.location("/public");
        chessService.initChessGame();

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "static/chess.hbs");
        });
        post("/restart", (req, res) -> {
            chessService.initChessGame();
            return "보드 초기화 성공!";
        }, jsonTransformer);
        post("/move", (req, res) -> {
            PositionDto positionDTO = jsonTransformer.getGson().fromJson(req.body(), PositionDto.class);
            return chessService.move(positionDTO);
        }, jsonTransformer);
        post("/currentBoard", (req, res) -> {
            return chessService.getCurrentBoard();
        }, jsonTransformer);
        post("/currentTurn", (req, res) -> {
            return chessService.turnName();
        }, jsonTransformer);
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
