package chess.controller;

import chess.domain.command.Commands;
import chess.domain.dto.MoveDto;
import chess.service.SparkChessService;
import chess.view.ModelView;
import chess.view.RenderView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class SparkController {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final SparkChessService sparkChessService;

    public SparkController(SparkChessService sparkChessService) {
        this.sparkChessService = sparkChessService;
    }

    public String moveToMainPage(Request request, Response response) throws SQLException {
        return RenderView.renderHtml(ModelView.startResponse(sparkChessService.loadHistory()),
                "chessStart.html");
    }

    public String playNewGameWithNoSave(Request request, Response response) {
        return RenderView.renderHtml(ModelView.newGameResponse(sparkChessService.initialGameInfo()), "chessGame.html");
    }

    public String playNewGameWithSave(Request request, Response response) throws SQLException {
        return RenderView.renderHtml(ModelView.newGameResponse(
                sparkChessService.initialGameInfo(),
                sparkChessService.addHistory(request.params(":name"))
        ), "chessGame.html");
    }

    public String movePiece(Request request, Response response) {
        final MoveDto moveDto = GSON.fromJson(request.body(), MoveDto.class);
        final String command = makeMoveCmd(moveDto.getSource(), moveDto.getTarget());
        final String historyId = moveDto.getGameId();
        try {
            sparkChessService.move(historyId, command, new Commands(command));
            return GSON.toJson(ModelView.moveSparkResponse(sparkChessService.continuedGameInfo(historyId), historyId));
        } catch (IllegalArgumentException | SQLException e) {
            response.status(400);
            return e.getMessage();
        }
    }

    private String makeMoveCmd(String source, String target) {
        return String.join(" ", "move", source, target);
    }

    public String continueGame(Request request, Response response) throws SQLException {
        String id = sparkChessService.getIdByName(request.queryParams("name"));
        return RenderView.renderHtml(ModelView.commonResponseForm(sparkChessService.continuedGameInfo(id), id), "chessGame.html");
    }

    public String endGame(Request request, Response response) {
        return RenderView.renderHtml("chessGame.html");
    }
}
