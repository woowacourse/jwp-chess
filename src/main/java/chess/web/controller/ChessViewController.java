package chess.web.controller;

import chess.domain.board.Board;
import chess.domain.entity.Room;
import chess.web.controller.dto.BoardDto;
import chess.web.controller.dto.RoomRequest;
import chess.web.controller.dto.RoomResponse;
import chess.web.service.ChessService;
import chess.web.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ChessViewController {

    private final ChessService chessService;
    private final RoomService roomService;

    public ChessViewController(ChessService chessService, RoomService roomService) {
        this.chessService = chessService;
        this.roomService = roomService;
    }

    @GetMapping("/")
    public String getRoomList(Model model) {
        List<Room> rooms = roomService.getRoomList();
        List<RoomResponse.IdAndTitle> titles = rooms.stream()
                .map(RoomResponse.IdAndTitle::new)
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
        model.addAttribute("board", BoardDto.from(id, board));
        return "room";
    }
}
