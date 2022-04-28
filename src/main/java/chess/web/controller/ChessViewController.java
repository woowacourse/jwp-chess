package chess.web.controller;

import chess.domain.Room;
import chess.web.controller.dto.RoomResponseDto;
import chess.web.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ChessViewController {

    private final ChessService chessService;

    public ChessViewController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String getRoomList(Model model) {
        List<Room> rooms = chessService.getRoomList();
        List<RoomResponseDto.Title> titles = rooms.stream().
                map(RoomResponseDto.Title::new)
                .collect(Collectors.toList());
        model.addAttribute("list", titles);
        return "index";
    }
}
