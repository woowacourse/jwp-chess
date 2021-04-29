package chess.controller;

import chess.dto.RoomListDto;
import chess.dto.response.ScoreResponseDto;
import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ChessController {
    private final ChessService chessService;

    public ChessController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/room-list")
    public String roomList(final Model model) {
        List<RoomListDto> roomsDto = new ArrayList<>();
        Map<Long, String> rooms = chessService.rooms();
        for (Map.Entry<Long, String> roomEntry : rooms.entrySet()) {
            roomsDto.add(new RoomListDto(roomEntry.getKey(), roomEntry.getValue()));
        }
        model.addAttribute("rooms", roomsDto);
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
