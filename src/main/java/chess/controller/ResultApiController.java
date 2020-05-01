package chess.controller;

import chess.model.dto.GameResultDto;
import chess.model.dto.UserNameDto;
import chess.model.dto.UserNamesDto;
import chess.service.ResultService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/result")
public class ResultApiController {
	private final ResultService resultService;

	public ResultApiController(ResultService resultService) {
		this.resultService = resultService;
	}

	@GetMapping("viewUsers")
	public UserNamesDto viewUsers() {
		return resultService.getUsers();
	}

	@PostMapping("userResult")
	public GameResultDto userResult(@RequestBody UserNameDto userNameDto) {
		return resultService.getResult(userNameDto);
	}
}
