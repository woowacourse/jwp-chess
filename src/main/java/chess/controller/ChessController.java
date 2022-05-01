package chess.controller;

import chess.dto.GameDeleteDto;
import chess.dto.GameDeleteResponseDto;
import chess.dto.MoveRequestDto;
import chess.dto.RoomRequestDto;
import chess.dto.ScoreDto;
import chess.model.room.Room;
import chess.service.ChessService;
import chess.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChessController {

    private final ChessService chessService;
    private final RoomService roomService;

    public ChessController(ChessService chessService, RoomService roomService) {
        this.chessService = chessService;
        this.roomService = roomService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("rooms", roomService.getRooms());
        return "home";
    }

    @PostMapping("/room")
    public String room(RoomRequestDto roomDto) {
        Room room = chessService.init(
                roomDto.getRoomName(),
                roomDto.getPassword(),
                roomDto.getWhiteName(),
                roomDto.getBlackName());

        return "redirect:/room/" + room.getId();
    }

    @GetMapping("/room/{roomId}")
    public String room(Model model, @PathVariable int roomId) {
        model.addAttribute("roomId", roomId);
        model.addAttribute("board", chessService.getBoard(roomId));
        return "chess-game";
    }

    @PostMapping("/room/{roomId}/move")
    @ResponseBody
    public ResponseEntity<Boolean> move(@RequestBody MoveRequestDto moveRequestDto, @PathVariable int roomId) {
        String source = moveRequestDto.getSource();
        String target = moveRequestDto.getTarget();
        chessService.move(source, target, roomId);
        return ResponseEntity.ok(chessService.isEnd(roomId));
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

    @DeleteMapping("/room")
    @ResponseBody
    public GameDeleteResponseDto delete(@RequestBody GameDeleteDto gameDeleteDto) {
        return roomService.deleteRoom(gameDeleteDto.getId(), gameDeleteDto.getPassword());
    }
}
