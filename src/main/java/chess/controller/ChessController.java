package chess.controller;

import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

import chess.dao.GameDaoImpl;
import chess.dao.PieceDaoImpl;
import chess.db.DBConnector;
import chess.domain.command.MoveCommand;
import chess.dto.ErrorResponseDto;
import chess.serviece.ChessService;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class ChessController {

    private static final String STATIC_FILE_LOCATION = "/static";
    private static final String VIEW_NAME = "index.html";

    private final ChessService chessService;
    private final HandlebarsTemplateEngine templateEngine;

    public ChessController() {
        final DBConnector dbConnector = new DBConnector();
        this.chessService = new ChessService(new PieceDaoImpl(dbConnector), new GameDaoImpl(dbConnector));
        this.templateEngine = new HandlebarsTemplateEngine();
    }

    public void run() {
        final Gson gson = new Gson();
        staticFileLocation(STATIC_FILE_LOCATION);

        get("/chess-game", (req, res) -> render(new HashMap<>()));

        get("/chess-game/start", (req, res) -> gson.toJson(chessService.initializeGame()));

        get("/chess-game/load", (req, res) -> gson.toJson(chessService.getChess()));

        post("/chess-game/move", (req, res) -> gson.toJson(chessService.movePiece(parseToMoveCommand(req))));

        get("/chess-game/score", (req, res) -> gson.toJson(chessService.getScore()));

        post("/chess-game/end", (req, res) -> gson.toJson(chessService.finishGame()));

        exception(Exception.class, (exception, request, response) -> {
            System.out.println(exception.getMessage());
            response.status(500);
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(exception.getMessage());
            response.body(gson.toJson(errorResponseDto));
        });
    }

    private String render(Map<String, Object> model) {
        return templateEngine.render(new ModelAndView(model, VIEW_NAME));
    }

    private MoveCommand parseToMoveCommand(Request request) {
        String source = request.queryParams("source");
        String target = request.queryParams("target");
        return MoveCommand.of(source, target);
    }
}
