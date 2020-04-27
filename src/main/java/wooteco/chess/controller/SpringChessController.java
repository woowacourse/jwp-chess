package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import wooteco.chess.service.ChessService;
import wooteco.chess.service.dto.ChessBoardDto;
import wooteco.chess.service.dto.ChessGameDto;
import wooteco.chess.service.dto.ChessStatusDtos;

@Controller
public class SpringChessController {

	private final ChessService chessService;

	public SpringChessController(final ChessService chessService) {
		this.chessService = chessService;
	}

	@GetMapping("/")
	public String start() {
		return "index";
	}

	@GetMapping("/chess")
	public String loadChessGame(final Model model) {
		final ChessGameDto chessGameDto = chessService.loadChessGame();

		return renderGame(chessGameDto, model);
	}

	@PostMapping("/chess_play")
	public String playChessGame(@RequestParam final String sourcePosition, @RequestParam final String targetPosition,
		final Model model) {
		final ChessGameDto chessGameDto = chessService.playChessGame(sourcePosition.trim(), targetPosition.trim());

		if (chessGameDto.isEndState()) {
			return renderResult(chessGameDto, model);
		}
		return renderGame(chessGameDto, model);
	}

	@PostMapping("/chess_new")
	public String newChessGame(final Model model) {
		return renderGame(chessService.createChessGame(), model);
	}

	@PostMapping("/chess_end")
	public String endChessGame(final Model model) {
		final ChessGameDto chessGameDto = chessService.endChessGame();
		return renderResult(chessGameDto, model);
	}

	private String renderGame(final ChessGameDto chessGameDto, final Model model) {
		final ChessBoardDto chessBoardDto = chessGameDto.getChessBoardDto();
		final ChessStatusDtos chessStatusDtos = chessGameDto.getChessStatusDtos();

		model.addAllAttributes(chessBoardDto.getChessBoard());
		model.addAttribute("piece_color", chessGameDto.getPieceColorDto());
		model.addAttribute("status", chessStatusDtos.getChessStatusDtos());
		return "chess";
	}

	private String renderResult(final ChessGameDto chessGameDto, final Model model) {
		final ChessBoardDto chessBoardDto = chessGameDto.getChessBoardDto();
		final ChessStatusDtos chessStatusDtos = chessGameDto.getChessStatusDtos();

		model.addAllAttributes(chessBoardDto.getChessBoard());
		model.addAttribute("is_king_caught", chessGameDto.isKingCaught());
		model.addAttribute("piece_color", chessGameDto.getPieceColorDto());
		model.addAttribute("status", chessStatusDtos.getChessStatusDtos());
		return "result";
	}

}
