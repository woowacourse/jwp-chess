package wooteco.chess.controller;

import static spark.Spark.*;
import static wooteco.chess.utils.json.JsonUtil.*;
import static wooteco.chess.view.response.ResponseStatus.*;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Supplier;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import wooteco.chess.service.GameService;
import wooteco.chess.view.dto.requestdto.PositionRequestDTO;
import wooteco.chess.view.response.StandardResponse;

public class SparkChessController {
	private static final HandlebarsTemplateEngine TEMPLATE_ENGINE = new HandlebarsTemplateEngine();
	private static final ModelAndView DEFAULT_MODEL_AND_VIEW = new ModelAndView(new HashMap<String, Object>(),
		"index.hbs");

	private final GameService gameService;

	public SparkChessController(GameService gameService) {
		this.gameService = Objects.requireNonNull(gameService);
	}

	public void run() {
		get("/", (req, res) -> TEMPLATE_ENGINE.render(DEFAULT_MODEL_AND_VIEW));

		get("/chess/state", (req, res) ->
				makeResponse(() ->
					new StandardResponse(SUCCESS, toJsonTree(gameService.getCurrentState()))),
			json()
		);

		post("/chess/state", (req, res) ->
				makeResponse(() -> {
					gameService.changeState(req.body());
					return new StandardResponse(SUCCESS);
				}),
			json()
		);

		get("/chess/pieces", (req, res) ->
				makeResponse(() ->
					new StandardResponse(SUCCESS, toJsonTree(this.gameService.findAllPiecesOnBoard()))
				),
			json()
		);

		get("/chess/record", (req, res) ->
				makeResponse(() ->
					new StandardResponse(SUCCESS, toJsonTree(this.gameService.calculateScore()))
				),
			json()
		);

		post("/chess/move", (req, res) ->
				makeResponse(() -> {
					PositionRequestDTO requestDTO = fromJson(req.body(), PositionRequestDTO.class);
					this.gameService.move(requestDTO);
					return new StandardResponse(SUCCESS, toJsonTree(gameService.findChangedPiecesOnBoard(requestDTO)));
				}),
			json()
		);

		get("/chess/isnotfinish", (req, res) ->
				makeResponse(() ->
					new StandardResponse(SUCCESS, toJsonTree(this.gameService.isNotFinish()))
				),
			json()
		);

		get("/chess/result", (req, res) ->
				makeResponse(() ->
					new StandardResponse(SUCCESS, toJsonTree(this.gameService.getWinner()))
				),
			json()
		);
	}

	private StandardResponse makeResponse(Supplier<StandardResponse> responseGenerator) {
		try {
			return responseGenerator.get();
		} catch (RuntimeException e) {
			return new StandardResponse(ERROR, e.getMessage());
		}
	}
}
