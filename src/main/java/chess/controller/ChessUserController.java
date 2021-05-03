package chess.controller;

import chess.dto.request.UserCreateRequest;
import chess.dto.request.UserLoginRequest;
import chess.dto.response.UserResponse;
import chess.service.ChessUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class ChessUserController {

    private final ChessUserService chessUserService;

    @Autowired
    public ChessUserController(final ChessUserService chessUserService) {
        this.chessUserService = chessUserService;
    }

    @GetMapping("/{name}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String name) {
        return ResponseEntity.ok(chessUserService.user(name));
    }

    @PostMapping()
    public ResponseEntity create(@Valid @RequestBody UserCreateRequest request) {
        chessUserService.create(request.getUserName(), request.getUserPw());
        return ResponseEntity.ok().build();

    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody UserLoginRequest request) {
        UserResponse response = chessUserService.login(request.getUserName(), request.getUserPw());
        return ResponseEntity.ok(response);
    }
}
