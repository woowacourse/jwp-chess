package wooteco.chess.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
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
	public String index(Model model) {
		model.addAttribute("gameNames", chessService.showAllGames());
		return "index";
	}

	@PostMapping("/game/new")
	public String createGame(@RequestParam("name") final String name, final Model model, HttpServletResponse response) {
		ChessGameDto chessGameDto = chessService.createChessGame(name);
		Cookie cookie = new Cookie("gameId", String.valueOf(chessGameDto.getId()));

		cookie.setPath("/");
		response.addCookie(cookie);
		return renderGame(chessGameDto, model);
	}

	@PostMapping("/game")
	public String showGame(@RequestParam("name") final String name, final Model model, HttpServletResponse response) {
		ChessGameDto chessGameDto = chessService.loadChessGameByName(name);
		Long gameId = chessGameDto.getId();

		if (chessService.isEndGame(gameId)) {
			return renderResult(chessGameDto, model);
		}

		Cookie cookie = new Cookie("gameId", String.valueOf(gameId));
		cookie.setPath("/");
		response.addCookie(cookie);
		return renderGame(chessGameDto, model);
	}

	@PostMapping("/game/play")
	public String playChessGame(@CookieValue("gameId") Cookie gameIdCookie, @RequestParam final String sourcePosition,
		@RequestParam final String targetPosition, final Model model) {
		final Long gameId = Long.parseLong(gameIdCookie.getValue());
		final ChessGameDto chessGameDto = chessService.playChessGame(gameId, sourcePosition.trim(),
			targetPosition.trim());

		if (chessGameDto.isEndState()) {
			return renderResult(chessGameDto, model);
		}
		return renderGame(chessGameDto, model);
	}

	@PostMapping("/game/end")
	public String endChessGame(@CookieValue("gameId") Long gameId, final Model model) {
		final ChessGameDto chessGameDto = chessService.endChessGame(gameId);
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
