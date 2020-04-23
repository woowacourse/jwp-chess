package wooteco.chess.controller;

import org.springframework.stereotype.Controller;

import wooteco.chess.service.ChessService;

@Controller
public class SpringChessController {

	private final ChessService chessService;

	public SpringChessController(final ChessService chessService) {
		this.chessService = chessService;
	}

}
