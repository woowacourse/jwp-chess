package chess.controller.web;

import chess.chessgame.domain.room.Room;
import chess.chessgame.domain.room.game.ChessGameManager;
import chess.chessgame.domain.room.user.User;
import chess.controller.web.dto.ErrorMessageResponseDto;
import chess.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String enterRoom(@PathVariable long roomId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        validateUser(roomId, user);
        ChessGameManager game = roomService.findGameBy(roomId);
        model.addAttribute("gameId", game.getId());
        return "chess";
    }

    private void validateUser(long roomId, User user) {
        if (Objects.isNull(user)) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "방에 입장할 권한이 없습니다.");
        }
        isProperUser(roomId, user);
    }

    private void isProperUser(long roomId, User user) {
        Room room = roomService.findRoomByUserId(user.getUserId());
        if (room.getRoomId() != roomId) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "방에 입장할 권한이 없습니다.");
        }
    }

    @ExceptionHandler(HttpClientErrorException.class)
    private ResponseEntity<ErrorMessageResponseDto> handle(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessageResponseDto(e.getMessage()));
    }
}
