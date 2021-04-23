package chess.controller.spring;

import chess.dto.LoginRequestDTO;
import chess.service.spring.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO, HttpSession httpSession) {
        String password = loginRequestDTO.getPassword();
        int roomId = loginRequestDTO.getRoomId();
        userService.addUserIntoRoom(roomId, password);
        httpSession.setAttribute("roomId", String.valueOf(roomId));
        httpSession.setAttribute("password", password);
        return ResponseEntity.status(HttpStatus.OK).body("/chessgame/" + roomId);
    }

    @DeleteMapping("/logout/{roomId}")
    public ResponseEntity<String> logout(@PathVariable int roomId, HttpSession httpSession) {
        String password = (String) httpSession.getAttribute("password");
        userService.deleteUserBy(roomId, password);
        httpSession.invalidate();
        String location = "/";
        return ResponseEntity.status(HttpStatus.OK).body(location);
    }
}
