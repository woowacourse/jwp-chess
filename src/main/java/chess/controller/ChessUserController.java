package chess.controller;

import chess.domain.User;
import chess.service.ChessUserService;
import dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class ChessUserController {

    private final ChessUserService chessUserService;

    @Autowired
    public ChessUserController(final ChessUserService chessUserService) {
        this.chessUserService = chessUserService;
    }

    @PostMapping()
    public void create(@RequestBody UserDto userDto) {
        chessUserService.create(userDto.getName(), userDto.getPw());
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody UserDto userDto) {
        UserDto response = chessUserService.login(userDto.getName(), userDto.getPw());
        return ResponseEntity.ok(response);
    }
}
