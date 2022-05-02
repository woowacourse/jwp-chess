package chess.web.controller;

import chess.service.RoomService;
import chess.service.dto.request.CreateGameRequest;
import chess.service.dto.request.DeleteGameRequest;
import chess.service.dto.response.DeleteGameResponse;
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

    private final RoomService roomService;

    public ChessController(final RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/")
    public String renderIndex(Model model) {
        model.addAttribute("allRooms", roomService.getAllRooms());
        return "index";
    }

    @GetMapping("/rooms/{roomId}/game")
    public String renderBoard(@PathVariable Integer roomId) {
        return "board";
    }

    @PostMapping("/room")
    public String createRoom(CreateGameRequest createGameRequest) {
        String name = createGameRequest.getName();
        String password = createGameRequest.getPassword();
        roomService.createRoom(name, password);
        return "redirect:/";
    }

    @ResponseBody
    @DeleteMapping("/room")
    public DeleteGameResponse deleteRoom(@RequestBody DeleteGameRequest deleteRequest) {
        int id = deleteRequest.getId();
        String password = deleteRequest.getPassword();
        return roomService.deleteRoom(id, password);
    }
}
