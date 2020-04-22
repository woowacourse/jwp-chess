package chess.controller;

import static spark.Spark.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import chess.service.ChessGameService;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class WebChessController {
	private ChessGameService chessGameService = new ChessGameService();

	public WebChessController() throws SQLException {
	}

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
			Map<String, Object> model = settingModels(new HashMap<>());
			return render(model, "index.html");
		});

		get("/new-chess-game", (req, res) -> {
			this.chessGameService.setNewChessGame();

			Map<String, Object> model = settingModels(new HashMap<>());
			return render(model, "index.html");
		});

		post("/move", (req, res) -> {
			Map<String, Object> model = new HashMap<>();

			String source = req.queryParams("source");
			String target = req.queryParams("target");

			try {
				this.chessGameService.movePiece(source, target);
				this.chessGameService.proceedGame();
			} catch (IllegalArgumentException e) {
				model.put("error", e.getMessage());
			}

			if (this.chessGameService.isGameOver()) {
				res.redirect("/winner");
			}

			settingModels(model);
			return render(model, "index.html");
		});

		get("/winner", (req, res) -> {
			if (!this.chessGameService.isGameOver()) {
				res.redirect("/");
				return redirect;
			}
			Map<String, Object> model = new HashMap<>();

			model.put("winner", this.chessGameService.getWinner());
			model.put("loser", this.chessGameService.getLoser());
			model.put("blackScore", this.chessGameService.getBlackPieceScore());
			model.put("whiteScore", this.chessGameService.getWhitePieceScore());
			this.chessGameService.endGame();

			return render(model, "winner.html");
		});
	}

	private Map<String, Object> settingModels(Map<String, Object> model) {
		model.put("cells", this.chessGameService.getCells());
		model.put("currentTeam", this.chessGameService.getCurrentTeam());
		model.put("blackScore", this.chessGameService.getBlackPieceScore());
		model.put("whiteScore", this.chessGameService.getWhitePieceScore());
		return model;
	}
}
