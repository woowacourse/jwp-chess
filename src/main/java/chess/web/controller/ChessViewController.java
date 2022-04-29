package chess.web.controller;

import chess.domain.board.Board;
import chess.domain.entity.Room;
import chess.web.controller.dto.BoardDto;
import chess.web.controller.dto.RoomRequestDto;
import chess.web.controller.dto.RoomResponseDto;
import chess.web.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
        List<RoomResponseDto> titles = rooms.stream().
                map(RoomResponseDto::new)
                .collect(Collectors.toList());

        model.addAttribute("list", titles);
        return "index";
    }

    @GetMapping("/new")
    public String getCreateForm() {
        return "room-create-form";
    }

    @PostMapping(path = "/new")
    public String createGame(RoomRequestDto roomRequestDto) {
        Long id = chessService.createRoom(roomRequestDto);
        return "redirect:/room/" + id;
    }

    @GetMapping("/room/{id}")
    public String getRoom(Model model, @PathVariable Long id) {
        Board board = chessService.loadGame(id);
        model.addAttribute("board", BoardDto.from(id, board));
        return "room";
    }
}
