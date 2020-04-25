package wooteco.chess.controller;

import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.dto.MoveRequestDto;
import wooteco.chess.dto.MoveResponseDto;
import wooteco.chess.service.ChessService;

public class WebController {
	private static final Gson gson = new Gson();
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

		post("/start", (req, res) -> {
			BoardDto boardDto = service.createGame();
			Map<String, Object> model = new HashMap<>();
			model.put("response", boardDto);
			return render(model, GAME_VIEW);
		});

		post("/move", (req, res) -> {
			MoveRequestDto moveRequestDto = gson.fromJson(req.body(), MoveRequestDto.class);
			MoveResponseDto moveResponseDto = service.move(moveRequestDto);

			return gson.toJson(moveResponseDto);
		});

		get("/save", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			model.put("message", "저장되었습니다.");
			return render(model, GAME_VIEW);
		});

		get("/load", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			BoardDto boardDto = service.load(Long.parseLong(req.queryParams("gameId")));
			model.put("response", boardDto);
			return render(model, GAME_VIEW);
		});

		exception(IllegalArgumentException.class, (exception, request, response) -> {
			response.status(400);
			response.body(gson.toJson(exception.getMessage()));
		});

		exception(IllegalStateException.class, (exception, request, response) -> {
			response.status(400);
			response.body(gson.toJson(exception.getMessage()));
		});
	}

	private static String render(Map<String, Object> model, String templatePath) {
		return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
	}
}
