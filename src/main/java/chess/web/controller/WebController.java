package chess.web.controller;

import chess.domain.command.Commands;
import chess.domain.vo.MoveVo;
import chess.web.service.WebChessService;
import chess.view.ModelView;
import chess.view.RenderView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class WebController {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final WebChessService webChessService;

    public WebController(WebChessService webChessService) {
        this.webChessService = webChessService;
    }

    public String moveToMainPage(Request request, Response response) throws SQLException {
        return RenderView.renderHtml(ModelView.startResponse(webChessService.loadHistory()),
                "chessStart.html");
    }

    public String playNewGameWithNoSave(Request request, Response response) {
        return RenderView.renderHtml(ModelView.newGameResponse(webChessService.initialGameInfo()), "chessGame.html");
    }

    public String playNewGameWithSave(Request request, Response response) throws SQLException {
        return RenderView.renderHtml(ModelView.newGameResponse(
                webChessService.initialGameInfo(),
                webChessService.addHistory(request.params(":name"))
        ), "chessGame.html");
    }

    public String movePiece(Request request, Response response) {
        final MoveVo moveVo = GSON.fromJson(request.body(), MoveVo.class);
        final String command = makeMoveCmd(moveVo.getSource(), moveVo.getTarget());
        final String historyId = moveVo.getGameId();
        try {
            webChessService.move(historyId, command, new Commands(command));
            return GSON.toJson(ModelView.moveResponse(webChessService.continuedGameInfo(historyId), historyId));
        } catch (IllegalArgumentException | SQLException e) {
            response.status(400);
            return e.getMessage();
        }
    }

    private String makeMoveCmd(String source, String target) {
        return String.join(" ", "move", source, target);
    }

    public String continueGame(Request request, Response response) throws SQLException {
        String id = webChessService.getIdByName(request.queryParams("name"));
        return RenderView.renderHtml(ModelView.commonResponseForm(webChessService.continuedGameInfo(id), id), "chessGame.html");
    }

    public String endGame(Request request, Response response) {
        return RenderView.renderHtml("chessGame.html");
    }
}
