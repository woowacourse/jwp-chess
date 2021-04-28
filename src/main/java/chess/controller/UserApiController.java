package chess.controller;

import chess.dto.user.UserRequestDto;
import chess.dto.user.UserResponseDto;
import chess.service.UserService;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/users")
@RestController
public class UserApiController {

    private final UserService userService;

    public UserApiController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRequestDto userRequestDto) {
        final long id = userService.add(userRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
            .location(URI.create("/users/" + id))
            .body("{}");
    }

    @GetMapping
    public ResponseEntity<UserResponseDto> findUserByName(@RequestParam String name) {
        return ResponseEntity.ok()
            .body(userService.findUserByName(name));
    }

}
