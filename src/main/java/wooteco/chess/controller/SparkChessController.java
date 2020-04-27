package wooteco.chess.controller;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;
import wooteco.chess.service.ChessService;
import wooteco.chess.service.dto.ChessBoardDto;
import wooteco.chess.service.dto.ChessGameDto;
import wooteco.chess.service.dto.ChessStatusDtos;

public class SparkChessController {

	private static final HandlebarsTemplateEngine HANDLEBARS_TEMPLATE_ENGINE = new HandlebarsTemplateEngine();

	private final ChessService chessService;

	public SparkChessController(final ChessService chessService) {
		Objects.requireNonNull(chessService, "체스 서비스가 null입니다.");
		this.chessService = chessService;
	}

	public void run() {
		get("/", this::renderStartPage);
		get("/chess", (request, response) -> renderGame(chessService.loadChessGame()));

		post("/chess_play", this::playChessGame);
		post("/chess_new", this::newChessGame);
		post("/chess_end", this::endChessGame);
	}

	private String renderStartPage(final Request request, final Response response) {
		return render(new HashMap<>(), "index.hbs");
	}

	private String playChessGame(final Request request, final Response response) {
		final String sourcePosition = request.queryParams("sourcePosition").trim();
		final String targetPosition = request.queryParams("targetPosition").trim();
		final ChessGameDto chessGameDto = chessService.playChessGame(sourcePosition, targetPosition);

		if (chessGameDto.isEndState()) {
			return renderResult(chessGameDto);
		}
		return renderGame(chessGameDto);
	}

	private String newChessGame(final Request request, final Response response) {
		return renderGame(chessService.createChessGame());
	}

	private String endChessGame(final Request request, final Response response) {
		return renderResult(chessService.endChessGame());
	}

	private String renderGame(final ChessGameDto chessGameDto) {
		final ChessBoardDto chessBoardDto = chessGameDto.getChessBoardDto();
		final ChessStatusDtos chessStatusDtos = chessGameDto.getChessStatusDtos();

		final Map<String, Object> model = new HashMap<>(chessBoardDto.getChessBoard());
		model.put("piece_color", chessGameDto.getPieceColorDto());
		model.put("status", chessStatusDtos.getChessStatusDtos());
		return render(model, "chess.hbs");
	}

	private String renderResult(final ChessGameDto chessGameDto) {
		final ChessBoardDto chessBoardDto = chessGameDto.getChessBoardDto();
		final ChessStatusDtos chessStatusDtos = chessGameDto.getChessStatusDtos();

		final Map<String, Object> model = new HashMap<>(chessBoardDto.getChessBoard());
		model.put("is_king_caught", chessGameDto.isKingCaught());
		model.put("piece_color", chessGameDto.getPieceColorDto());
		model.put("status", chessStatusDtos.getChessStatusDtos());
		return render(model, "result.hbs");
	}

	private static String render(Map<String, Object> model, String templatePath) {
		return HANDLEBARS_TEMPLATE_ENGINE.render(new ModelAndView(model, templatePath));
	}

}
