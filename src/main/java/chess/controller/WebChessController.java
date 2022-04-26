package chess.controller;

import static chess.domain.piece.Team.BLACK;
import static chess.domain.piece.Team.WHITE;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import chess.domain.Command;
import chess.domain.piece.Team;
import chess.service.ChessService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class WebChessController {

    private final ChessService chessService;

    public WebChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    public void run() {
        staticFiles.location("/static");

        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "index.html");
        });

        get("/error", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "error.html");
        });

        get("/start", (request,response) -> {
            Map<String, Object> model = new HashMap<>();
            try {
                String gameName = request.queryParams("game_name");
                List<String> chessBoard = chessService.findByName(gameName);

                model.put("chessboard", chessBoard);
            } catch (IllegalStateException e) {
                response.redirect("/error");
            }
            return render(model, "chess.html");
        });

        post("/move", (request,response) -> {
            String gameName = request.queryParams("game_name");
            List<String> chessBoard = chessService.getCurrentChessBoard(gameName);
            String moveCommand = makeCommand(request);

            Map<String, Object> model = new HashMap<>();

            try {
                chessBoard = chessService.move(gameName, moveCommand);
                if (chessService.isEnd(gameName)) {
                    response.redirect("/end");
                }
                model.put("chessboard", chessBoard);
            } catch(IllegalArgumentException e) {
                model.put("error", e.getMessage());
                model.put("chessboard", chessBoard);
            }

            return render(model, "chess.html");
        });

        get("/status", (request, response) -> {
            String gameName = request.queryParams("game_name");
            Map<Team, Double> score = chessService.getScore(gameName);
            List<String> chessBoard = chessService.getCurrentChessBoard(gameName);

            Map<String, Object> model = new HashMap<>();
            model.put("blackScore", score.get(BLACK));
            model.put("whiteScore", score.get(WHITE));
            model.put("chessboard", chessBoard);
            return render(model, "chess.html");
        });

        get("/end", (request, response) -> {
            String gameName = request.queryParams("game_name");
            String winTeamName = chessService.finish(gameName, Command.from("end"));
            List<String> chessBoard = chessService.getCurrentChessBoard(gameName);

            Map<String, Object> model = new HashMap<>();
            model.put("winTeam", winTeamName);
            model.put("chessboard", chessBoard);
            return render(model, "chess.html");
        });
    }

    private String makeCommand(Request request) {
        String from = request.queryParams("from");
        String to = request.queryParams("to");

        return "move " + from + " " + to;
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
