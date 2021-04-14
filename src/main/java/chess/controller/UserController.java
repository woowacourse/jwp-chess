package chess.controller;

import chess.dto.UserResponseDto;
import chess.entity.UserEntity;
import chess.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping("/users")
@Controller
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity userEntity){
        final long id = userService.add(userEntity);
        return ResponseEntity.created(URI.create("/users/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<UserResponseDto> findUserByName(@RequestParam String name) {
        return ResponseEntity.ok()
                .body(userService.findUserByName(name));
    }

}
