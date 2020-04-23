package wooteco.chess.controller;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.notFound;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.staticFileLocation;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;
import wooteco.chess.domain.piece.Position;
import wooteco.chess.service.ChessGameService;

public class SparkChessController {
	private final Gson gson;
	private final ChessGameService chessGameService;

	public SparkChessController(Gson gson, ChessGameService chessGameService) {
		this.gson = gson;
		this.chessGameService = chessGameService;
	}

	public void run() {
		staticFileLocation("static");

		get("/", this::index);
		get("/rooms", this::getRooms);
		post("/rooms", this::createRoom);

		get("/rooms/:id", this::renderRoom);
		put("/rooms/:id", this::restartRoom);
		delete("/rooms/:id", this::deleteRoom);

		get("/rooms/:id/board", this::getBoard);
		put("/rooms/:id/board", this::movePiece);

		notFound("<script>location.replace('/')</script>");
	}

	private String index(Request req, Response res) {
		return render(new HashMap<>(), "index.hbs");
	}

	private String getRooms(Request req, Response res) throws Exception {
		return gson.toJson(chessGameService.games());
	}

	private String createRoom(Request req, Response res) throws Exception {
		return gson.toJson(chessGameService.create());
	}

	private String renderRoom(Request req, Response res) {
		int id = Integer.parseInt(req.params(":id"));
		try {
			chessGameService.find(id);
			Map<String, Object> model = new HashMap<>();
			model.put("id", id);
			return render(model, "game.hbs");
		} catch (Exception e) {
			return "<script>location.replace('/')</script>";
		}
	}

	private String restartRoom(Request req, Response res) throws Exception {
		int id = Integer.parseInt(req.params(":id"));
		return gson.toJson(chessGameService.restart(id));
	}

	private String deleteRoom(Request req, Response res) throws Exception {
		int id = Integer.parseInt(req.params(":id"));
		return gson.toJson(chessGameService.delete(id));
	}

	private String getBoard(Request req, Response res) throws Exception {
		int id = Integer.parseInt(req.params(":id"));
		return gson.toJson(chessGameService.find(id));
	}

	private String movePiece(Request req, Response res) throws Exception {
		int id = Integer.parseInt(req.params(":id"));
		Position source = Position.from(req.queryParams("source"));
		Position target = Position.from(req.queryParams("target"));
		return gson.toJson(chessGameService.move(id, source, target));
	}

	private String render(Map<String, Object> model, String templatePath) {
		return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
	}
}
