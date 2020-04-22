package wooteco.chess.controller;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.service.ChessService;

public class WebController {
	private static final String GAME_VIEW = "game.html";

	private ChessService service;

	public WebController(ChessService service) {
		this.service = service;
	}

	public void run() {
		port(8080);
		staticFileLocation("/templates");

		get("/", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			return render(model, GAME_VIEW);
		});

		get("/start", (req, res) -> {
			BoardDto boardDto = service.createGame();
			Map<String, Object> model = new HashMap<>();
			model.put("response", boardDto);
			return render(model, GAME_VIEW);
		});

		get("/move", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			try {
				BoardDto boardDto = service.move(Long.parseLong(req.queryParams("gameId")), req.queryParams("moveCommand"));
				model.put("response", boardDto);
			} catch (IllegalArgumentException e) {
				model.put("error", e);
			}
			return render(model, GAME_VIEW);
		});

		get("/save", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			model.put("message", "저장되었습니다.");
			return render(model, GAME_VIEW);
		});

		get("/loading", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			BoardDto boardDto = service.load(Long.parseLong(req.queryParams("gameId")));
			model.put("response", boardDto);
			return render(model, GAME_VIEW);
		});
	}

	private static String render(Map<String, Object> model, String templatePath) {
		return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
	}
}
