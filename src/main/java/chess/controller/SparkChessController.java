package chess.controller;

import chess.domain.ChessGame;
import chess.domain.dto.BoardDto;
import chess.domain.dto.MoveInfoDto;
import chess.domain.dto.ScoreDto;
import chess.domain.board.BoardFactory;
import chess.service.SparkChessService;
import com.google.gson.Gson;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

public class SparkChessController {
    public static final BoardFactory boardFactory = new BoardFactory();
    public final SparkChessService sparkChessService;

    public SparkChessController() {
        sparkChessService = new SparkChessService();
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }

    public void run() {
        ChessGame chessGame = new ChessGame();
        Gson gson = new Gson();

        get("/", (req, res) -> {
            return render(new HashMap<>(), "chess.html");
        });

        get("/loadSavedBoard", (req, res) -> {
            BoardDto savedBoardInfo = sparkChessService.getSavedBoardInfo(chessGame);
            return gson.toJson(savedBoardInfo);
        });

        get("/resetBoard", (req, res) -> {
            BoardDto boardDTO = sparkChessService.initiateBoard(chessGame);
            return gson.toJson(boardDTO);
        });

        get("/scoreStatus", (req, res) -> {
            ScoreDto scoreDTO = ScoreDto.of(chessGame.scoreStatus());
            return gson.toJson(scoreDTO);
        });

        post("/move", (req, res) -> {
            MoveInfoDto moveInfoDTO = gson.fromJson(req.body(), MoveInfoDto.class);
            BoardDto boardDTO = sparkChessService.move(chessGame, moveInfoDTO);
            return gson.toJson(boardDTO);
        });
    }
}