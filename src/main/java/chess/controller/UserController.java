package chess.controller;

import chess.dto.user.UserRequestDto;
import chess.dto.user.UserResponseDto;
import chess.service.UserService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/users")
@Controller
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody UserRequestDto userRequestDto) {
        final long id = userService.add(userRequestDto);
        return ResponseEntity.created(URI.create("/users/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<UserResponseDto> findUserByName(@RequestParam String name) {
        return ResponseEntity.ok()
            .body(userService.findUserByName(name));
    }

}
