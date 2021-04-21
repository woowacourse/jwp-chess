package chess.controller.spring;

import chess.dto.LoginRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpSession httpSession) {
        httpSession.setAttribute("roomId", String.valueOf(loginRequestDTO.getRoomId()));
        httpSession.setAttribute("password", loginRequestDTO.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body("/chessgame/" + loginRequestDTO.getRoomId());
    }
}
