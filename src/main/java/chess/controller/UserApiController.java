package chess.controller;

import chess.entity.User;
import chess.service.LoginUser;
import chess.service.UserService;
import chess.service.dto.UserFindResponseDto;
import chess.service.dto.UserSaveRequestDto;
import chess.service.dto.UserSignRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1/users")
public class UserApiController {

    private static final String USER = "user";

    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> save(@RequestBody UserSaveRequestDto requestDto, HttpSession session){
        userService.save(requestDto);
        session.setAttribute(USER, requestDto.getName());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<UserFindResponseDto> find(@LoginUser User user){
        UserFindResponseDto responseDto = userService.findByName(user.getName());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> signIn(@RequestBody UserSignRequestDto requestDto, HttpSession session){
        userService.signIn(requestDto);
        session.setAttribute(USER, requestDto.getName());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
