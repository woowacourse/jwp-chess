package chess.controller;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.*;
import chess.service.ChessService;
import com.google.gson.Gson;
import spark.ModelAndView;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.sql.SQLException;
import java.util.*;

import static spark.Spark.get;
import static spark.Spark.post;

public class SpringChessController {
    public static final Gson GSON = new Gson();

    private final ChessService chessService;

    public SpringChessController(final ChessService chessService) {
        this.chessService = chessService;
    }

    public void run() {
        get("/", (req, res) -> render(new HashMap<>(), "home.html"));

        get("/start", (req, res) -> makeNewGame(res));

        get("/reset", (req, res) -> makeNewGame(res));

        get("/chess", (req, res) -> {
            Map<String, String> chessBoardFromDB = chessService.chessBoardFromDB();
            Map<Position, Piece> chessBoard = chessService.chessBoard(chessBoardFromDB);
            Map<String, String> stringChessBoard = chessService.stringChessBoard(chessBoard);
            PiecesDto piecesDto = chessService.piecesDto(chessBoard);

            String jsonFormatChessBoard = GSON.toJson(stringChessBoard);
            Map<String, Object> model = new HashMap<>();
            model.put("jsonFormatChessBoard", jsonFormatChessBoard);

            chessService.updateRound(piecesDto);

            String currentTurn = chessService.currentTurn();
            model.put("currentTurn", currentTurn);

            chessService.changeRoundState(currentTurn);

            PlayerDto playerDto = chessService.playerDto();
            ScoreDto scoreDto = chessService.scoreDto(playerDto);
            chessService.changeRoundToEnd(playerDto);

            model.put("whiteScore", scoreDto.getWhiteScore());
            model.put("blackScore", scoreDto.getBlackScore());

            return render(model, "chess.html");
        });

        post("/move", (req, res) -> {
            MoveRequestDto moveRequestDto = GSON.fromJson(req.body(), MoveRequestDto.class);
            Queue<String> commands =
                    new ArrayDeque<>(Arrays.asList("move", moveRequestDto.getSource(), moveRequestDto.getTarget()));

            res.type("application/json");

            try {
                chessService.executeRound(commands);
            } catch (RuntimeException runtimeException) {
                return "{\"status\":\"500\", \"message\":\"" + runtimeException.getMessage() + "\"}";
            }
            chessService.movePiece(moveRequestDto);
            return "{\"status\":\"200\", \"message\":\"성공\"}";
        });

        post("/turn", (req, res) -> {
            TurnChangeRequestDto turnChangeRequestDto = GSON.fromJson(req.body(), TurnChangeRequestDto.class);
            chessService.changeTurn(turnChangeRequestDto);
            return null;
        });
    }

    private Object makeNewGame(final Response res) throws SQLException {
        chessService.remove();
        chessService.makeRound();
        Map<Position, Piece> chessBoard = chessService.chessBoard();
        Map<String, String> filteredChessBoard = chessService.filteredChessBoard(chessBoard);
        chessService.initialize(filteredChessBoard);

        res.redirect("/chess");
        return null;
    }

    private static String render(final Map<String, Object> model, final String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
