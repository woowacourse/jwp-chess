package chess.controller;

import chess.domain.dto.RoomListDto;
import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ViewController {
    private final ChessService chessService;

    public ViewController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public ModelAndView mainView() {
        ModelAndView mv = new ModelAndView();
        RoomListDto roomListDto = chessService.getRoomList();
        mv.setViewName("roomList");
        mv.addObject("roomList", roomListDto.getRoomList());
        return mv;
    }

    @PostMapping("/")
    public String roomList(Model model, @RequestParam("roomName") String roomName) {
        chessService.addRoom(roomName);
        RoomListDto roomListDto = chessService.getRoomList();
        model.addAttribute("roomList", roomListDto.getRoomList());
        return "roomList";
    }
}
