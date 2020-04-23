package wooteco.chess.controller;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import wooteco.chess.domain.Result;
import wooteco.chess.domain.chessboard.Board;
import wooteco.chess.domain.chesspiece.Piece;
import wooteco.chess.domain.position.Position;
import wooteco.chess.service.ChessService;

public class SparkController {
	private static final HandlebarsTemplateEngine HANDLEBARS_TEMPLATE_ENGINE = new HandlebarsTemplateEngine();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private final ChessService chessService;

	public SparkController(ChessService chessService) {
		this.chessService = chessService;
	}

	private static String getBoardJson(Board board) {
		Map<String, Object> model = new HashMap<>();
		for (Piece piece : board.findAll()) {
			model.put(piece.getPosition().getString(), piece.getName());
		}
		return GSON.toJson(model);
	}

	private static String render(Map<String, Object> model, String templatePath) {
		return HANDLEBARS_TEMPLATE_ENGINE.render(new ModelAndView(model, templatePath));
	}

	public void run() {
		get("/", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			return render(model, "index.hbs");
		});

		get("/init", (req, res) -> getBoardJson(chessService.find()));

		post("/move", (req, res) -> {
			Position startPosition = Position.of(req.queryParams("startPosition"));
			Position targetPosition = Position.of(req.queryParams("targetPosition"));
			Board board = chessService.move(startPosition, targetPosition);
			return getBoardJson(board);
		});

		get("/isEnd", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			if (!chessService.isEnd()) {
				model.put("isEnd", false);
				return GSON.toJson(model);
			}
			model.put("isEnd", true);
			if (chessService.isWinWhiteTeam()) {
				model.put("message", "WHITE팀 승리!");
				return GSON.toJson(model);
			}
			model.put("message", "BLACK팀 승리!");
			return GSON.toJson(model);
		});

		get("/restart", (req, res) -> getBoardJson(chessService.restart()));

		get("/status", (req, res) -> {
			ChessService chessService = new ChessService();
			Result result = chessService.status();
			Map<String, Object> model = new HashMap<>();
			model.put("blackTeamScore", result.getBlackTeamScore());
			model.put("whiteTeamScore", result.getWhiteTeamScore());
			return GSON.toJson(model);
		});

		exception(IllegalArgumentException.class,
			((exception, request, response) -> response.body(exception.getMessage())));
		exception(UnsupportedOperationException.class,
			((exception, request, response) -> response.body(exception.getMessage())));
	}

}
