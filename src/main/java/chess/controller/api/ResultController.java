package chess.controller.api;

import chess.dto.repository.GameResultDto;
import chess.dto.repository.UserNamesDto;
import chess.service.ResultService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/result")
public class ResultController {

    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping("/users")
    public ResponseEntity<UserNamesDto> getUsers() {
        UserNamesDto userNamesDto = resultService.getUsers();
        return new ResponseEntity<>(userNamesDto, HttpStatus.OK);
    }

    @GetMapping("/userResult")
    public ResponseEntity<GameResultDto> userResult(@RequestParam String userName) {
        GameResultDto gameResultDto = resultService.getResult(userName);
        return new ResponseEntity<>(gameResultDto, HttpStatus.OK);
    }
}
