// package wooteco.chess;
//
// import static spark.Spark.*;
//
// import java.util.HashMap;
// import java.util.Map;
//
// import spark.ModelAndView;
// import spark.Spark;
// import spark.template.handlebars.HandlebarsTemplateEngine;
// import wooteco.chess.domain.position.Position;
// import wooteco.chess.service.GameManagerService;
// import wooteco.chess.util.WebOutputRenderer;
//
// public class SparkChessApplication {
// 	public static void main(String[] args) {
// 		Spark.staticFileLocation("static");
// 		final GameManagerService gameManagerService = new GameManagerService();
//
// 		get("/", (request, response) -> "index.html");
//
// 		get("/start", (request, response) -> {
// 			gameManagerService.resetGame();
// 			Map<String, Object> model = new HashMap<>();
// 			model.put("piecesDto", WebOutputRenderer.toPiecesDto(gameManagerService.getBoard()));
// 			model.put("turn", gameManagerService.getCurrentTurn().name());
//
// 			return render(model, "board.hbs");
// 		});
//
// 		get("/resume", (request, response) -> {
// 			Map<String, Object> model = new HashMap<>();
// 			model.put("piecesDto", WebOutputRenderer.toPiecesDto(gameManagerService.getBoard()));
// 			model.put("turn", gameManagerService.getCurrentTurn().name());
//
// 			return render(model, "board.hbs");
// 		});
//
// 		post("/move", (request, response) -> {
// 			Map<String, Object> model = new HashMap<>();
// 			try {
// 				gameManagerService.move(Position.of(request.queryParams("target")),
// 					Position.of(request.queryParams("destination")));
// 			} catch (RuntimeException e) {
// 				model.put("error", e.getMessage());
// 			}
// 			model.put("piecesDto", WebOutputRenderer.toPiecesDto(gameManagerService.getBoard()));
// 			model.put("turn", gameManagerService.getCurrentTurn().name());
//
// 			if (!gameManagerService.isKingAlive()) {
// 				model.put("winner", gameManagerService.getCurrentTurn().reverse());
// 				gameManagerService.resetGame();
// 				return render(model, "end.hbs");
// 			}
// 			return render(model, "board.hbs");
// 		});
//
// 		get("/status", (request, response) -> {
// 			Map<String, Object> model = new HashMap<>();
// 			model.put("piecesDto", WebOutputRenderer.toPiecesDto(gameManagerService.getBoard()));
// 			model.put("turn", gameManagerService.getCurrentTurn().name());
// 			model.put("scores", WebOutputRenderer.scoreToModel(gameManagerService.calculateEachScore()));
//
// 			return render(model, "board.hbs");
// 		});
//
// 		get("/end", (request, response) -> {
// 			gameManagerService.resetGame();
// 			return render(new HashMap<>(), "end.hbs");
// 		});
// 	}
//
// 	public static String render(Map<String, Object> model, String templatePath) {
// 		return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
// 	}
// }
