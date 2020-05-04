package wooteco.chess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import wooteco.chess.domain.game.NormalStatus;
import wooteco.chess.domain.position.MovingPosition;
import wooteco.chess.dto.ChessGameDto;
import wooteco.chess.dto.GamesDto;
import wooteco.chess.dto.MovablePositionsDto;
import wooteco.chess.dto.MoveStatusDto;
import wooteco.chess.service.SpringDataJDBCChessService;
import wooteco.chess.web.JsonTransformer;

import java.util.HashMap;
import java.util.Map;

@Controller
public class SpringChessController {
	@Autowired
	private SpringDataJDBCChessService springDataJDBCChessService;

	@GetMapping("/")
	public ModelAndView routeMainPage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("index");
		modelAndView.addObject("normalStatus", NormalStatus.YES.isNormalStatus());
		return modelAndView;
	}

	@GetMapping("/games")
	@ResponseBody
	public String showGames() {
		GamesDto games = springDataJDBCChessService.selectAvailableGames();
		return JsonTransformer.toJson(games);
	}

	@PostMapping("/new")
	@ResponseBody
	public String saveNewGame(@RequestBody Map<String, Object> param) {
		System.out.println(param.get("gameName"));
		ChessGameDto chessGameDto = springDataJDBCChessService.createGameBy((String) param.get("gameName"));

		return JsonTransformer.toJson(chessGameDto);
	}

	@GetMapping("/game/{id}")
	@ResponseBody
	public ModelAndView startGame() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("chess");
		modelAndView.addObject("normalStatus", NormalStatus.YES.isNormalStatus());
		return modelAndView;
	}

	@GetMapping("/board/{id}")
	@ResponseBody
	public String setBoard(@PathVariable Long id) {
		ChessGameDto chessGameDto = springDataJDBCChessService.setBoardBy(id);
		return JsonTransformer.toJson(chessGameDto);
	}

	@GetMapping("/board/{id}/source")
	@ResponseBody
	public String getMovablePositions(@PathVariable Long id, @RequestParam String source) {
		Map<String, Object> model = new HashMap<>();
		try {
			MovablePositionsDto movablePositionsDto = springDataJDBCChessService.findMovablePositions(id, source);
			model.put("movable", movablePositionsDto.getMovablePositionNames());
			model.put("position", movablePositionsDto.getPosition());
			model.put("normalStatus", NormalStatus.YES.isNormalStatus());
			return JsonTransformer.toJson(model);
		} catch (IllegalArgumentException | UnsupportedOperationException | NullPointerException e) {
			model.put("normalStatus", NormalStatus.NO.isNormalStatus());
			model.put("exception", e.getMessage());
			return JsonTransformer.toJson(model);
		}
	}

	@GetMapping("/board/{id}/destination")
	@ResponseBody
	public String checkMovable(@PathVariable Long id, @RequestParam String startPosition,
							   @RequestParam String destination) {
		Map<String, Object> model = new HashMap<>();
		MoveStatusDto moveStatusDto = springDataJDBCChessService.checkMovable(id,
				new MovingPosition(startPosition, destination));

		model.put("normalStatus", moveStatusDto.getNormalStatus());
		model.put("exception", moveStatusDto.getException());
		return JsonTransformer.toJson(model);
	}

	@PostMapping("/board/{id}")
	@ResponseBody
	public String saveHistory(@PathVariable Long id, @RequestBody MovingPosition movingPosition) {
		try {
			MoveStatusDto moveStatusDto = springDataJDBCChessService.move(id, movingPosition);
			return JsonTransformer.toJson(moveStatusDto);
		} catch (IllegalArgumentException e) {
			MoveStatusDto moveStatusDto = new MoveStatusDto(false, e.getMessage());
			return JsonTransformer.toJson(moveStatusDto);
		}
	}

	@GetMapping("/loading/{id}")
	public ModelAndView loadGame() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("chess");
		modelAndView.addObject("normalStatus", NormalStatus.YES.isNormalStatus());
		return modelAndView;
	}

	@GetMapping("/result/{winner}")
	public ModelAndView showResult(@PathVariable String winner) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("result");
		modelAndView.addObject("winner", winner);
		return modelAndView;
	}
}