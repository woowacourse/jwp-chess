package chess.controller;

import chess.entity.UserEntity;
import chess.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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


}
