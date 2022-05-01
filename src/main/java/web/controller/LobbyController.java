package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.dao.RoomDao;
import web.dto.RoomDto;
import web.service.RoomService;


@Controller
public class LobbyController {

    private final RoomDao roomDao;
    private final RoomService roomService;

    public LobbyController(RoomDao roomDao, RoomService roomService) {
        this.roomDao = roomDao;
        this.roomService = roomService;
    }

    @GetMapping("/")
    public String lobby(Model model) {
        model.addAttribute("chess-games", roomDao.findAll());
        return "index";
    }

    @PostMapping("/create-chess-game")
    public String createChessGame(@RequestParam String name, @RequestParam String password) {
        RoomDto room = roomService.saveRoom(name, password);
        return "redirect:/chess-game?chess-game-id=" + room.getChessGameId() ;
    }

    @PostMapping("/remove-chess-game")
    public String removeChessGame(@RequestParam int id, @RequestParam String password) {
        roomService.deleteRoom(id, password);
        return "redirect:/";
    }
}
