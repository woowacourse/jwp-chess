package chess.controller;

import chess.dto.ResponseDto;
import chess.dto.ScoreDto;
import chess.model.room.Room;
import chess.service.ChessService;
import org.eclipse.jetty.http.HttpStatus;
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
    public String move(@RequestBody MoveRequest moveRequest, @PathVariable int roomId) {
        try {
            chessService.move(moveRequest.getSource(), moveRequest.getTarget(), roomId);
        } catch (IllegalArgumentException e) {
            return ResponseDto.of(HttpStatus.BAD_REQUEST_400, e.getMessage(), chessService.isEnd(roomId)).toString();
        }
        return ResponseDto.of(HttpStatus.OK_200, null, chessService.isEnd(roomId)).toString();
    }

    @GetMapping("/room/{roomId}/status")
    @ResponseBody
    public String status(@PathVariable int roomId) {
        return ScoreDto.from(chessService.status(roomId)).toString();
    }

    @PostMapping("/room/{roomId}/end")
    public String end(Model model, @PathVariable int roomId) {
        model.addAttribute("result", ScoreDto.from(chessService.status(roomId)));
        chessService.end(roomId);
        return "result";
    }
}
