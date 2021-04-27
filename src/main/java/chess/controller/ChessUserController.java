package chess.controller;

import chess.service.ChessUserService;
import dto.RoomRequestDto;
import dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class ChessUserController {

    private final ChessUserService chessUserService;

    @ExceptionHandler({MissingRequestCookieException.class})
    public ResponseEntity<String> checkCookieMissing(Exception exception) {
        return ResponseEntity.badRequest().body("로그인을 해 주세요.");
    }

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
