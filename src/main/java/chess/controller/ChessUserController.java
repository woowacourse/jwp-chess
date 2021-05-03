package chess.controller;

import chess.dto.request.UserCreateRequest;
import chess.dto.request.UserLoginRequest;
import chess.dto.response.UserResponse;
import chess.service.ChessUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Objects;

@RestController
@RequestMapping("/api/user")
public class ChessUserController {

    private final ChessUserService chessUserService;

    @Autowired
    public ChessUserController(final ChessUserService chessUserService) {
        this.chessUserService = chessUserService;
    }

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<String> checkDuplicatedException(Exception exception) {
        return ResponseEntity.badRequest().body("중복 된 이름이 있습니다.");
    }

    @GetMapping("/{name}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String name) {
        return ResponseEntity.ok(chessUserService.user(name));
    }

    @PostMapping()
    public ResponseEntity create(@Valid @RequestBody UserCreateRequest request,
                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException(
                    Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        chessUserService.create(request.getUserName(), request.getUserPw());
        return ResponseEntity.ok().build();

    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody UserLoginRequest request) {
        UserResponse response = chessUserService.login(request.getUserName(), request.getUserPw());
        return ResponseEntity.ok(response);
    }
}
