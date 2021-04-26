package chess.controller;

import chess.service.UserService;
import chess.service.dto.UserFindResponseDto;
import chess.service.dto.UserSaveRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1/users")
public class UserApiController {

    private static final String USER = "user";
    private static final String PASSWORD = "password";

    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody UserSaveRequestDto requestDto, HttpSession session){
        userService.save(requestDto);
        session.setAttribute(USER, requestDto.getName());
        session.setAttribute(PASSWORD, requestDto.getPassword());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<UserFindResponseDto> find(HttpSession session){
        String name = (String) session.getAttribute(USER);
        UserFindResponseDto responseDto = userService.findByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
