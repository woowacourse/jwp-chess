package chess.controller;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

import chess.model.domain.piece.Team;
import chess.model.dto.CreateRoomDto;
import chess.model.dto.DeleteRoomDto;
import chess.model.dto.MoveDto;
import chess.model.dto.PromotionTypeDto;
import chess.model.dto.SourceDto;
import chess.model.dto.UserNameDto;
import chess.service.ChessGameService;
import chess.service.ResultService;
import chess.service.RoomService;
import com.google.gson.Gson;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class ChessUIController {}
/*
    private static final Gson GSON = new Gson();
    private static final ChessGameService CHESS_GAME_SERVICE = ChessGameService.getInstance();
    private static final RoomService ROOM_SERVICE = new RoomService();
    private static final ResultService RESULT_SERVICE = ResultService.getInstance();

    public static void run() {
        staticFileLocation("/static");

        post("/followGame", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            Map<Team, String> userNames = getUserNames(req);
            int roomId = Integer.parseInt(req.queryParams("roomId"));
            model.put("gameId", CHESS_GAME_SERVICE.getIdBefore(roomId, userNames));
            return render(model, "/game.hbs");
        });

        post("/game/newGame", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            Map<Team, String> userNames = getUserNames(req);
            model.put("gameId", CHESS_GAME_SERVICE
                .endAndNewChessGame(Integer.parseInt(req.queryParams("gameId")), userNames));
            return render(model, "/game.hbs");
        });

        post("/game/choiceGame", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("roomId",
                CHESS_GAME_SERVICE.getRoomId(Integer.parseInt(req.queryParams("gameId"))));
            return render(model, "/start.hbs");
        });

        post("/game/api/move", (req, res) -> {
            MoveDto moveDTO = GSON.fromJson(req.body(), MoveDto.class);
            return GSON.toJson(CHESS_GAME_SERVICE.move(moveDTO));
        });

        post("/game/api/path", (req, res) -> {
            SourceDto sourceDto = GSON.fromJson(req.body(), SourceDto.class);
            return GSON.toJson(CHESS_GAME_SERVICE.getPath(sourceDto));
        });

        post("/game/api/promotion", (req, res) -> {
            PromotionTypeDto promotionTypeDTO = GSON.fromJson(req.body(), PromotionTypeDto.class);
            return GSON.toJson(CHESS_GAME_SERVICE.promotion(promotionTypeDTO));
        });

        post("/game/api/board", (req, res) -> {
            MoveDto moveDTO = GSON.fromJson(req.body(), MoveDto.class);
            return GSON.toJson(CHESS_GAME_SERVICE.loadChessGame(moveDTO.getGameId()));
        });

        post("/game/api/end", (req, res) -> {
            MoveDto moveDTO = GSON.fromJson(req.body(), MoveDto.class);
            return GSON.toJson(CHESS_GAME_SERVICE.endGame(moveDTO));
        });

        post("/result", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "/result.hbs");
        });

        get("/result/api/viewUsers", (req, res) -> GSON.toJson(RESULT_SERVICE.getUsers()));

        post("/result/api/userResult", (req, res) -> GSON
            .toJson(RESULT_SERVICE.getResult(GSON.fromJson(req.body(), UserNameDto.class))));
    }

    public static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
*/
