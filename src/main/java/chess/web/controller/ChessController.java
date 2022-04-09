package chess.web.controller;

import static chess.domain.piece.Team.BLACK;
import static chess.domain.piece.Team.WHITE;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import chess.domain.Command;
import chess.domain.piece.Team;
import chess.web.service.ChessService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    public void run() {
        staticFiles.location("/static");

        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "index.html");
        });

        get("/start", (request,response) -> {
            String gameName = request.queryParams("game_name");
            List<String> chessBoard = chessService.findByName(gameName);

            Map<String, Object> model = new HashMap<>();
            model.put("chessboard", chessBoard);
            return render(model, "chess.html");
        });

        post("/move", (request,response) -> {
            List<String> chessBoard = chessService.getCurrentChessBoard();
            String moveCommand = makeCommand(request);

            Map<String, Object> model = new HashMap<>();

            try {
                chessBoard = chessService.move(moveCommand);
                if (chessService.isEnd()) {
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
            Map<Team, Double> score = chessService.getScore();
            List<String> chessBoard = chessService.getCurrentChessBoard();

            Map<String, Object> model = new HashMap<>();
            model.put("blackScore", score.get(BLACK));
            model.put("whiteScore", score.get(WHITE));
            model.put("chessboard", chessBoard);
            return render(model, "chess.html");
        });

        get("/end", (request, response) -> {
            String winTeamName = chessService.finish(Command.from("end"));
            List<String> chessBoard = chessService.getCurrentChessBoard();

            Map<String, Object> model = new HashMap<>();
            model.put("winTeam", winTeamName);
            model.put("chessboard", chessBoard);
            return render(model, "chess.html");
        });

        get("/save", (request, response) -> {
            chessService.save();
            List<String> chessBoard = chessService.getCurrentChessBoard();

            Map<String, Object> model = new HashMap<>();
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
