package chess.web.controller;

import chess.domain.entity.Room;
import chess.domain.board.Board;
import chess.web.controller.dto.RoomResponseDto;
import chess.web.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
        List<RoomResponseDto.TitleAndId> titles = rooms.stream().
                map(RoomResponseDto.TitleAndId::new)
                .collect(Collectors.toList());

        model.addAttribute("list", titles);
        return "index";
    }

    @GetMapping("/new")
    public String getCreateForm() {
        return "room-create-form";
    }

    @GetMapping("/room/{id}")
    public String getRoom(Model model, @PathVariable Long id) {
        Board board = chessService.loadGame(id);
        model.addAttribute("board", board);
        return "room";
    }
}
