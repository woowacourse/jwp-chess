package wooteco.chess;

import wooteco.chess.controller.WebController;
import wooteco.chess.service.ChessService;

public class WebUIChessApplication {
	public static void main(String[] args) {
		ChessService service = new ChessService();
		WebController webController = new WebController(service);
		webController.run();
	}
}
