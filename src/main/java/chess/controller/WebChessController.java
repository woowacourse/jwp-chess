package chess.controller;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.Map;

import chess.domain.GameResult;
import chess.domain.board.ChessBoard;
import chess.dto.CellManager;
import chess.service.ChessGameService;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class WebChessController {
	private ChessGameService chessGameService = new ChessGameService();

	private static String render(Map<String, Object> model, String templatePath) {
		return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
	}

	public void run() {
		port(8080);
		staticFiles.location("/static");

		get("/", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			return render(model, "index.html");
		});

		get("/chess-game", (req, res) -> {
			ChessBoard chessBoard = chessGameService.loadBoard();
			Map<String, Object> model = settingModels(new HashMap<>(), chessBoard);
			return render(model, "index.html");
		});

		get("/new-chess-game", (req, res) -> {
			ChessBoard chessBoard = this.chessGameService.createNewChessGame();
			Map<String, Object> model = settingModels(new HashMap<>(), chessBoard);
			return render(model, "index.html");
		});

		post("/move", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			String source = req.queryParams("source");
			String target = req.queryParams("target");

			ChessBoard chessBoard;

			try {
				chessBoard = this.chessGameService.movePiece(source, target);
			} catch (IllegalArgumentException e) {
				chessBoard = chessGameService.loadBoard();
				model.put("error", e.getMessage());
			}

			if (chessBoard.isGameOver()) {
				res.redirect("/winner");
				return redirect;
			}

			settingModels(model, chessBoard);
			return render(model, "index.html");
		});

		get("/winner", (req, res) -> {
			ChessBoard chessBoard = this.chessGameService.loadBoard();

			if (!chessBoard.isGameOver()) {
				res.redirect("/");
				return redirect;
			}
			GameResult gameResult = chessBoard.createGameResult();
			Map<String, Object> model = new HashMap<>();

			model.put("winner", gameResult.getWinner());
			model.put("loser", gameResult.getLoser());
			model.put("blackScore", gameResult.getAliveBlackPieceScoreSum());
			model.put("whiteScore", gameResult.getAliveWhitePieceScoreSum());
			this.chessGameService.endGame();

			return render(model, "winner.html");
		});
	}

	private Map<String, Object> settingModels(Map<String, Object> model, ChessBoard chessBoard) {
		GameResult gameResult = chessBoard.createGameResult();
		CellManager cellManager = new CellManager();

		model.put("cells", cellManager.createCells(chessBoard));
		model.put("currentTeam", chessBoard.getTeam().getName());
		model.put("blackScore", gameResult.getAliveBlackPieceScoreSum());
		model.put("whiteScore", gameResult.getAliveWhitePieceScoreSum());
		return model;
	}
}
