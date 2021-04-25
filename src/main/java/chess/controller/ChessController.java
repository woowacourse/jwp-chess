package chess.controller;

import chess.dto.response.RoomResponseDto;
import chess.dto.response.ScoreResponseDto;
import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ChessController {
    private final ChessService chessService;

    public ChessController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/room-list")
    public String roomList(final Model model) {
        List<RoomResponseDto> rooms = chessService.rooms();
        model.addAttribute("rooms", rooms);
        return "room-list";
    }

    @GetMapping("/chess/{roomId}")
    public String chess(@PathVariable final Long roomId, final Model model) {
        ScoreResponseDto scoreResponseDto = chessService.scoreResponseDto(roomId);
        model.addAttribute("roomId", roomId);
        model.addAttribute("score", scoreResponseDto);
        return "chess";
    }
}
