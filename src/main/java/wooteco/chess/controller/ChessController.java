package wooteco.chess.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import wooteco.chess.dto.BoardDto;
import wooteco.chess.dto.MoveRequestDto;
import wooteco.chess.dto.MoveResponseDto;
import wooteco.chess.dto.RoomsDto;
import wooteco.chess.service.ChessService;

@Controller
public class ChessController {

	private final ChessService chessService;

	public ChessController(ChessService chessService) {
		this.chessService = chessService;
	}

	@GetMapping("/")
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView("index");
		RoomsDto roomsDto = chessService.findAllGames();
		mv.addObject("rooms", roomsDto);
		return mv;
	}

	@PostMapping("/start")
	public ModelAndView start() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("game");
		mv.addObject("response", chessService.createGame());
		return mv;
	}

	@ResponseBody
	@PostMapping("/move")
	public ResponseEntity<MoveResponseDto> move(@RequestBody MoveRequestDto moveRequestDto) {
		MoveResponseDto moveResponseDto = chessService.move(moveRequestDto);
		return new ResponseEntity<>(moveResponseDto, HttpStatus.OK);
	}

	@GetMapping("/load")
	public ModelAndView load(@RequestParam(name = "gameId") Long gameId) {
		ModelAndView mv = new ModelAndView();
		BoardDto boardDto = chessService.load(gameId);
		mv.setViewName("game");
		mv.addObject("response", boardDto);
		return mv;
	}
}