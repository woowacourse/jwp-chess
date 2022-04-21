package chess.web;

import chess.web.dao.RoomDao;
import chess.web.dto.RoomDto;
import chess.web.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SpringChessController {

    private RoomService roomService = new RoomService(new RoomDao());

    @GetMapping("/")
    public String login() {
        return "login.html";
    }


    @PostMapping(value = "/board")
    public String createRoom(@RequestParam String name) {
        RoomDto roomDto = roomService.create(name);
        return "redirect:/board?roomId=" + roomDto.getId();
    }

    @GetMapping("/board")
    public String board() {
        return "/board.html";
    }

}
