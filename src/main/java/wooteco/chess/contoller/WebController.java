package wooteco.chess.contoller;

import static spark.Spark.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;
import wooteco.chess.domain.dto.ChessGameDto;
import wooteco.chess.domain.dto.MoveDto;
import wooteco.chess.domain.service.ChessGameService;

public class WebController {
	private final ChessGameService chessGameService;

	public WebController(ChessGameService chessGameService) {
		this.chessGameService = chessGameService;
	}

	public void run() {
		staticFiles.location("/public");
		port(8080);

		get("/", this::renderMain);
		get("/api/rooms", this::getRooms);
		post("/join", this::joinRoom);
		post("/create", this::createRoom);
		put("/move", this::movePiece);
		post("/restart", this::restartGame);
	}

	private static String render(Map<String, Object> model, String templatePath) {
		return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
	}

	private String renderMain(Request request, Response response) {
		return render(new HashMap<>(), "index.html");
	}

	private String getRooms(Request request, Response response) throws SQLException {
		return new Gson().toJson(chessGameService.findAllRooms());
	}

	private String joinRoom(Request request, Response response) throws SQLException {
		Map<String, Object> model = new HashMap<>();
		try {
			ChessGameDto chessGameDto = chessGameService.load(request.queryParams("room-name"));
			model.put("chessGame", chessGameDto);
			return render(model, "chess.html");
		} catch (IllegalArgumentException e) {
			response.status(409);
			model.put("error", e.getMessage());
			return render(model, "index.html");
		}
	}

	private String createRoom(Request request, Response response) throws SQLException {
		Map<String, Object> model = new HashMap<>();
		try {
			ChessGameDto chessGameDto = chessGameService.create(request.queryParams("room-name"));
			model.put("chessGame", chessGameDto);
			return render(model, "chess.html");
		} catch (IllegalArgumentException e) {
			response.status(409);
			model.put("error", e.getMessage());
			return render(model, "index.html");
		}
	}

	private String movePiece(Request request, Response response) throws SQLException {
		Map<String, Object> model = new HashMap<>();
		try {
			MoveDto moveDto = new Gson().fromJson(request.body(), MoveDto.class);
			ChessGameDto chessGameDto = chessGameService.move(moveDto.getRoomName(), moveDto.getSource(),
					moveDto.getTarget());
			model.put("chessGame", chessGameDto);
		} catch (IllegalArgumentException e) {
			response.status(409);
			model.put("error", e.getMessage());
		}
		return render(model, "chess.html");
	}

	private String restartGame(Request request, Response response) throws SQLException {
		Map<String, Object> model = new HashMap<>();
		ChessGameDto chessGameDto = chessGameService.restart(request.queryParams("room-name"));
		model.put("chessGame", chessGameDto);
		return render(model, "chess.html");
	}
}