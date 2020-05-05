package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wooteco.chess.db.MoveHistory;
import wooteco.chess.dto.GameResponseDto;
import wooteco.chess.service.ChessGameService;
import wooteco.chess.service.ChessRoomService;

@Controller
public class ChessController {
    private final ChessGameService chessGameService;
    private final ChessRoomService chessRoomService;

    public ChessController(ChessGameService chessGameService, ChessRoomService chessRoomService) {
        this.chessGameService = chessGameService;
        this.chessRoomService = chessRoomService;
    }

    @GetMapping("/")
    public String chessGame(Model model) {
        model.addAttribute("rooms", chessRoomService.findRooms());
        return "index";
    }

    @PostMapping("/play")
    public String startGame(@RequestParam(value = "room_name") String roomName, Model model) {
        GameResponseDto gameResponseDto = chessRoomService.addRoom(roomName);
        model.addAttribute("game_info", gameResponseDto);
        return "game_room";
    }

    @PostMapping("/resume")
    public String resumeGame(@RequestParam(value = "room_id") Long roomId, Model model) {
        GameResponseDto gameResponseDto = chessGameService.resumeGame(roomId);
        model.addAttribute("game_info", gameResponseDto);
        return "game_room";
    }

    @PostMapping("/move")
    public String move(@RequestParam(value = "room_id") Long roomId, MoveHistory moveHistory, Model model) {
        GameResponseDto gameResponseDto = chessGameService.move(roomId, moveHistory);

        if (gameResponseDto.getEnd() != null) {
            chessRoomService.deleteRoom(roomId);
        }

        model.addAttribute("game_info", gameResponseDto);
        return "game_room";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String errorMessage(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "game_room";
    }
}
