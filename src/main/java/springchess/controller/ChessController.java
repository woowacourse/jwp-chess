package springchess.controller;

import chess.dto.ResponseDto;
import chess.dto.ScoreDto;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springchess.model.room.Room;
import springchess.service.ChessService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    public String room(@RequestBody String messageBody) {
        final List<String> createRoomInput = Arrays.stream(messageBody.strip().split("\n"))
                .map(s -> s.split("=")[1])
                .collect(Collectors.toList());

        Room room = chessService.init(
                createRoomInput.get(0),
                createRoomInput.get(1),
                createRoomInput.get(2));

        return "redirect:/room" + room.getId();
    }

    @GetMapping("/room/{roomId}")
    public String room(Model model, @PathVariable(value = "roomId") String roomId) {
        model.addAttribute("roomId", Integer.parseInt(roomId));
        model.addAttribute("board", chessService.getBoard(Integer.parseInt(roomId)));
        return "chess-game";
    }

    @PostMapping("/room/{roomId}/move")
    @ResponseBody
    public String move(@RequestBody String messageBody, @PathVariable(value = "roomId") String roomId) {
        final String[] split = messageBody.strip().split("=")[1].split(" ");
        String source = split[0];
        String target = split[1];
        try {
            chessService.move(source, target, Integer.parseInt(roomId));
        } catch (IllegalArgumentException e) {
            return ResponseDto.of(HttpStatus.BAD_REQUEST_400, e.getMessage(), chessService.isEnd(Integer.parseInt(roomId))).toString();
        }
        return ResponseDto.of(HttpStatus.OK_200, null, chessService.isEnd(Integer.parseInt(roomId))).toString();
    }

    @GetMapping("/room/{roomId}/status")
    @ResponseBody
    public String status(@PathVariable(value = "roomId") String roomId) {
        return ScoreDto.from(chessService.status(Integer.parseInt(roomId))).toString();
    }

    @PostMapping("/room/{roomId}/end")
    public String end(Model model, @PathVariable(value = "roomId") String roomId) {
        int id = Integer.parseInt(roomId);
        model.addAttribute("result", ScoreDto.from(chessService.status(id)));
        chessService.end(id);
        return "result";
    }
}
