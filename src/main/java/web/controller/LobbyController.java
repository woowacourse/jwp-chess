package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.dao.RoomDao;
import web.dto.RoomDto;
import web.exception.InvalidRoomRequestException;
import web.exception.IsRunningChessGameException;
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

    @ExceptionHandler(InvalidRoomRequestException.class)
    public String invalidRoomRequestHandler(RedirectAttributes attributes) {
        attributes.addFlashAttribute("hasError", true);
        attributes.addFlashAttribute("errorMessage", "잘못된 방 비밀번호입니다.");
        return "redirect:/";
    }

    @ExceptionHandler(IsRunningChessGameException.class)
    public String isRunningChessGameHandler(RedirectAttributes attributes) {
        attributes.addFlashAttribute("hasError", true);
        attributes.addFlashAttribute("errorMessage", "체스 게임이 진행 중입니다.");
        return "redirect:/";
    }
}
