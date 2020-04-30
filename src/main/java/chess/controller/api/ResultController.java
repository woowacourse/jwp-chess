package chess.controller.api;

import chess.dto.GameResultDto;
import chess.dto.UserNameDto;
import chess.dto.UserNamesDto;
import chess.service.ResultService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/result")
public class ResultController {

    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping("/result/users")
    public UserNamesDto getUsers() {
        return resultService.getUsers();
    }

    @PostMapping("/result/userResult")
    public GameResultDto userResult(@RequestBody UserNameDto userNameDto) {
        return resultService.getResult(userNameDto);
    }
}
