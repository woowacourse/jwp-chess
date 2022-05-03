package chess.controller;

import chess.dto.MessageBody;
import chess.dto.ScoreDto;
import chess.model.room.Room;
import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ChessViewController {

    private final ChessService chessService;

    public ChessViewController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("rooms", chessService.getRooms());
        return "home";
    }

    @PostMapping("/room")
    public String createRoom(@ModelAttribute MessageBody messageBody) {

        Room room = chessService.init(
                messageBody.getRoomName(),
                messageBody.getWhiteName(),
                messageBody.getBlackName(),
                messageBody.getPassword());

        return "redirect:/room/" + room.getId();
    }

    @GetMapping("/room/{roomId}")
    public String showRoom(Model model, @PathVariable int roomId) {
        model.addAttribute("roomId", roomId);
        model.addAttribute("board", chessService.getBoard(roomId));
        return "chess-game";
    }

    @PostMapping("/room/{roomId}/end")
    public String end(Model model, @PathVariable int roomId) {
        model.addAttribute("result", ScoreDto.from(chessService.status(roomId)));
        chessService.end(roomId);
        return "result";
    }
}
