package chess.web.apicontroller;

import chess.service.UserService;
import chess.web.dto.user.UserRequestDto;
import chess.web.dto.user.UserResponseDto;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @PostMapping("/authentication")
    public ResponseEntity<UserResponseDto> authenticateUser(
        @RequestBody UserRequestDto userRequestDto) {

        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.authenticate(userRequestDto));
    }

}
