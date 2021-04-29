package chess.controller;

import chess.chessgame.domain.room.Room;
import chess.chessgame.domain.room.game.ChessGameManager;
import chess.chessgame.domain.room.user.User;
import chess.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@RequestMapping("/room")
@Controller
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("{roomId:[\\d]+}")
    public String enterRoom(@PathVariable long roomId, Model model, @SessionAttribute User user) {
        if (roomService.isAuthorityUser(user, roomId)) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "방에 입장할 수 없습니다.");
        }
        ChessGameManager game = roomService.findGameBy(roomId);
        model.addAttribute("gameId", game.getId());
        model.addAttribute("roomId", roomId);
        return "chess";
    }
}
