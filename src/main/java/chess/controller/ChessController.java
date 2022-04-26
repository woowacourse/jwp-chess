package chess.controller;

import chess.dto.ScoreDto;
import chess.model.room.Room;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("rooms", chessService.getRooms());
        return "home";
    }

    @PostMapping("/room")
    public String room(@ModelAttribute MessageBody messageBody) {

        Room room = chessService.init(
                messageBody.getRoomName(),
                messageBody.getWhiteName(),
                messageBody.getBlackName());

        return "redirect:/room/" + room.getId();
    }

    @GetMapping("/room/{roomId}")
    public String room(Model model, @PathVariable int roomId) {
        model.addAttribute("roomId", roomId);
        model.addAttribute("board", chessService.getBoard(roomId));
        return "chess-game";
    }

    @PatchMapping("/room/{roomId}/move")
    @ResponseBody
    public ResponseEntity<Boolean> move(@RequestBody MoveRequest moveRequest, @PathVariable int roomId) {
        chessService.move(moveRequest.getSource(), moveRequest.getTarget(), roomId);
        return ResponseEntity.ok().body(chessService.isEnd(roomId));
    }

    @GetMapping("/room/{roomId}/status")
    @ResponseBody
    public ResponseEntity<ScoreDto> status(@PathVariable int roomId) {
        return ResponseEntity.ok().body(ScoreDto.from(chessService.status(roomId)));
    }

    @PostMapping("/room/{roomId}/end")
    public String end(Model model, @PathVariable int roomId) {
        model.addAttribute("result", ScoreDto.from(chessService.status(roomId)));
        chessService.end(roomId);
        return "result";
    }
}
