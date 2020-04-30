package chess.controller.api;

import chess.dto.GameResultDto;
import chess.dto.UserNameDto;
import chess.dto.UserNamesDto;
import chess.service.ResultService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<UserNamesDto> getUsers() {
        UserNamesDto userNamesDto = resultService.getUsers();
        return new ResponseEntity<>(userNamesDto, HttpStatus.OK);
    }

    @PostMapping("/result/userResult")
    public ResponseEntity<GameResultDto> userResult(@RequestBody UserNameDto userNameDto) {
        GameResultDto gameResultDto = resultService.getResult(userNameDto);
        return new ResponseEntity<>(gameResultDto, HttpStatus.OK);
    }
}
