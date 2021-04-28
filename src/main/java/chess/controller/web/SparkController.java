package chess.controller.web;

import chess.chessgame.domain.room.game.ChessGameManager;
import chess.chessgame.domain.room.game.ChessGameManagerBundle;
import chess.chessgame.domain.room.game.board.position.Position;
import chess.service.ChessServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.ModelAndView;
import spark.Route;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class SparkController {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final int HTTP_STATUS_ERROR = 400;

    private final ChessServiceImpl chessServiceImpl;

    public SparkController(ChessServiceImpl chessServiceImpl) {
        this.chessServiceImpl = chessServiceImpl;
    }

    public void start() {
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "index.html");
        });

        gameRouting();

        exceptionHandling();
    }

    private String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }

    private void gameRouting() {
        get("/user", userRoute(), GSON::toJson);

        path("/game", () -> {
            get("/start", startRoute(), GSON::toJson);
            get("/score/:id", scoreRoute(), GSON::toJson);
            get("/load/:id", loadRoute(), GSON::toJson);
            post("/move", moveRoute(), GSON::toJson);
        });
    }

    private Route userRoute() {
        return (request, response) -> {
            response.type("application/json; charset=utf-8");
            ChessGameManagerBundle runningGames = chessServiceImpl.findRunningGames();
            return new RunningGameResponseDto(runningGames.getIdAndNextTurn());
        };
    }

    private Route startRoute() {
        return (request, response) -> {
            response.type("application/json; charset=utf-8");
            return new ChessGameResponseDto(chessServiceImpl.start());
        };
    }

    private Route scoreRoute() {
        return (request, response) -> {
            long id = Long.parseLong(request.params("id"));
            response.type("application/json; charset=utf-8");
            return new ScoreResponseDto(chessServiceImpl.getStatistics(id));
        };
    }

    private Route loadRoute() {
        return (request, response) -> {
            try {
                long id = Long.parseLong(request.params("id"));
                ChessGameManager load = chessServiceImpl.load(id);
                return new ChessGameResponseDto(load);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("게임 id는 숫자값이어야 합니다.");
            }
        };
    }

    private Route moveRoute() {
        return (request, response) -> {
            MoveRequestDto moveRequestDto = GSON.fromJson(request.body(), MoveRequestDto.class);
            chessServiceImpl.move(moveRequestDto.getGameId(), Position.of(moveRequestDto.getFrom()), Position.of(moveRequestDto.getTo()));
            response.type("application/json; charset=utf-8");
            return new MoveResponseDto(chessServiceImpl.isEnd(moveRequestDto.getGameId()), chessServiceImpl.nextColor(moveRequestDto.getGameId()));
        };
    }

    private void exceptionHandling() {
        exception(RuntimeException.class, (e, request, response) -> {
            response.type("application/json; charset=utf-8");
            response.status(HTTP_STATUS_ERROR);
            response.body(GSON.toJson(new ErrorMessageResponseDto(e.getMessage())));
        });
    }
}
