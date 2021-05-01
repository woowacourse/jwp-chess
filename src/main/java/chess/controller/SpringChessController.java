package chess.controller;

import chess.dto.RoomInfoRequestDto;
import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class SpringChessController {

    private final ChessService chessService;

    public SpringChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("names", chessService.roomNames());
        return "index";
    }

    @PostMapping("/white-game")
    public String whiteStart(Model model, HttpSession session, @ModelAttribute RoomInfoRequestDto roomInfoRequestDto) {
        String room = roomInfoRequestDto.getRoomName();
        String password = roomInfoRequestDto.getPassword();

        model.addAttribute("room", room);
        session.setAttribute("password", password);

        return "game";
    }

    @PostMapping("/black-game")
    public String blackStart(Model model, HttpSession session, @ModelAttribute RoomInfoRequestDto roomInfoRequestDto) {
        String room = roomInfoRequestDto.getRoomName();
        String password = roomInfoRequestDto.getPassword();

        chessService.deleteRoomFromList(room);

        model.addAttribute("room", room);
        session.setAttribute("password", password);

        return "game";
    }
}
