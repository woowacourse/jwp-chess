package wooteco.chess.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import chess.domain.GameResult;
import chess.domain.board.ChessBoard;
import chess.dto.CellManager;
import wooteco.chess.service.ChessGameService;

// hbs로 바꾸고, REST -> controller 로 바꿨는데 됐다
@Controller
public class ChessController {
	@Autowired
	private ChessGameService chessGameService;

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/chess-game")
	public String chessGame(Model model) throws SQLException {
		ChessBoard chessBoard = chessGameService.loadBoard();
		settingModels(model, chessBoard);
		return "index";
	}

	@GetMapping("/new-chess-game")
	public String newChessGame(Model model) throws SQLException {
		ChessBoard chessBoard = chessGameService.createNewChessGame();
		settingModels(model, chessBoard);
		return "index";
	}

	@GetMapping("/winner")
	public String winner(Model model) throws SQLException {
		ChessBoard chessBoard = chessGameService.loadBoard();

		if (!chessBoard.isGameOver()) {
			return "redirect:/";
		}
		GameResult gameResult = chessBoard.createGameResult();
		model.addAttribute("winner", gameResult.getWinner());
		model.addAttribute("loser", gameResult.getLoser());
		model.addAttribute("blackScore", gameResult.getAliveBlackPieceScoreSum());
		model.addAttribute("whiteScore", gameResult.getAliveWhitePieceScoreSum());
		chessGameService.endGame();

		return "winner";
	}

	@PostMapping("/move")
	public String winner(@RequestParam(defaultValue = "") String source,
		@RequestParam(defaultValue = "") String target,
		Model model) throws SQLException {
		ChessBoard chessBoard;
		try {
			chessBoard = chessGameService.movePiece(source, target);
		} catch (IllegalArgumentException e) {
			chessBoard = chessGameService.loadBoard();
			model.addAttribute("error", e.getMessage());
		}

		if (chessBoard.isGameOver()) {
			return "redirect:/winner";
		}
		settingModels(model, chessBoard);
		return "index";
	}

	private void settingModels(Model model, ChessBoard chessBoard) {
		GameResult gameResult = chessBoard.createGameResult();
		CellManager cellManager = new CellManager();

		model.addAttribute("cells", cellManager.createCells(chessBoard));
		model.addAttribute("currentTeam", chessBoard.getTeam().getName());
		model.addAttribute("blackScore", gameResult.getAliveBlackPieceScoreSum());
		model.addAttribute("whiteScore", gameResult.getAliveWhitePieceScoreSum());
	}
}
