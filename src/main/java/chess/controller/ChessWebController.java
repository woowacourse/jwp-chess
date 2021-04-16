package chess.controller;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.staticFiles;

import chess.JsonTransformer;
import chess.service.ChessService;
import chess.service.dto.ChessSaveRequestDto;
import chess.service.dto.CommonResponseDto;
import chess.service.dto.GameStatusRequestDto;
import chess.service.dto.MoveRequestDto;
import chess.service.dto.ResponseCode;
import chess.service.dto.TilesDto;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class ChessWebController {

    private final ChessService chessService;

    public ChessWebController(final ChessService chessService) {
        this.chessService = chessService;
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public void run() {
        staticFiles.location("/static");

        final JsonTransformer jsonTransformer = new JsonTransformer();

        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "main.html");
        });

        get("/games", (request, response) -> {
            TilesDto tilesDto = chessService.emptyBoard();
            Map<String, Object> model = new HashMap<>();
            model.put("tilesDto", tilesDto);
            return render(model, "board.html");
        });

        post("/games", (request, response) -> {
            ChessSaveRequestDto requestDto = new Gson().fromJson(request.body(), ChessSaveRequestDto.class);
            return chessService.startChess(requestDto);
        }, jsonTransformer);

        put("/games", (request, response) -> {
            GameStatusRequestDto requestDto = new Gson().fromJson(request.body(), GameStatusRequestDto.class);
            chessService.changeGameStatus(requestDto);
            return new CommonResponseDto<>(ResponseCode.OK.code(), ResponseCode.OK.message());
        }, jsonTransformer);

        get("/games/:name", (request, response) -> {
            String name = request.params(":name");
            return chessService.loadChess(name);
        }, jsonTransformer);

        put("/pieces", (request, response) -> {
            MoveRequestDto requestDto = new Gson().fromJson(request.body(), MoveRequestDto.class);
            return chessService.movePiece(requestDto);
        }, jsonTransformer);
    }
}
