package chess.controller;

import chess.service.ChessUserService;
import dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChessUserController {

    private final ChessUserService chessUserService;

    @Autowired
    public ChessUserController(final ChessUserService chessUserService) {
        this.chessUserService = chessUserService;
    }

    @PostMapping("/user")
    public void create(@RequestBody UserDto userDto) {
        chessUserService.create(userDto.getName(), userDto.getPw());
    }
}
