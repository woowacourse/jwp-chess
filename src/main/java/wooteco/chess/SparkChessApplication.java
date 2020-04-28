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
// 		get("/", (request, response) -> {
// 			Map<String, Object> model = new HashMap<>();
// 			model.put("roomNumbers", gameManagerService.getAllRoomNo());
//
// 			return render(model, "index.hbs");
// 		});
//
// 		get("/start", (request, response) -> {
// 			int roomNo = gameManagerService.newGame();
// 			response.redirect("/board/" + roomNo);
//
// 			return null;
// 		});
//
// 		get("/board/:roomNo", (request, response) -> {
// 			Map<String, Object> model = new HashMap<>();
// 			int roomNo = Integer.parseInt(request.params(":roomNo"));
//
// 			model.put("roomNo", roomNo);
// 			model.put("piecesDto", WebOutputRenderer.toPiecesDto(gameManagerService.getBoard(roomNo)));
// 			model.put("turn", gameManagerService.getCurrentTurn(roomNo).name());
// 			model.put("scores", WebOutputRenderer.scoreToModel(gameManagerService.calculateEachScore(roomNo)));
//
// 			return render(model, "board.hbs");
// 		});
//
// 		post("/move", (request, response) -> {
// 			Map<String, Object> model = new HashMap<>();
// 			int roomNo = Integer.parseInt(request.queryParams("roomNo"));
// 			try {
// 				gameManagerService.move(Position.of(request.queryParams("target")),
// 					Position.of(request.queryParams("destination")), roomNo);
// 			} catch (RuntimeException e) {
// 				model.put("error", e.getMessage());
// 				model.put("roomNo", roomNo);
// 				return render(model, "error.hbs");
// 			}
//
// 			if (!gameManagerService.isKingAlive(roomNo)) {
// 				model.put("winner", gameManagerService.getCurrentTurn(roomNo).reverse());
// 				gameManagerService.deleteGame(roomNo);
// 				return render(model, "end.hbs");
// 			}
// 			response.redirect("/board/" + roomNo);
// 			return null;
// 		});
//
// 		get("/end", (request, response) -> {
// 			int roomNo = Integer.parseInt(request.queryParams("roomNo"));
// 			gameManagerService.deleteGame(roomNo);
//
// 			return render(new HashMap<>(), "end.hbs");
// 		});
// 	}
//
// 	public static String render(Map<String, Object> model, String templatePath) {
// 		return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
// 	}
// }