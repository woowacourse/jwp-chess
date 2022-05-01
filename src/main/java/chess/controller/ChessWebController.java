package chess.controller;

import chess.domain.pieces.Piece;
import chess.dto.response.BoardDto;
import chess.dto.response.RoomDto;
import chess.dto.response.RoomsDto;
import chess.entities.GameEntity;
import chess.service.GameService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChessWebController {

    private final GameService gameService;

    public ChessWebController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<RoomDto> boardsDto = new ArrayList<>();
        List<GameEntity> boards = gameService.findAllBoard();
        for (GameEntity board : boards) {
            boardsDto.add(new RoomDto(board.getId(), board.getRoomTitle(), board.getMembers().get(0),
                    board.getMembers().get(1)));
        }
        model.addAttribute("boards", new RoomsDto(boardsDto));
        return "home";
    }

    @GetMapping("/room/{roomId}")
    public String showRoom(@PathVariable("roomId") int id, Model model) {
        model.addAttribute("roomId", id);
        GameEntity board = gameService.getBoard(id);
        Map<String, Piece> pieces = gameService.getPieces(id);
        model.addAttribute("board",
                BoardDto.of(pieces, board.getRoomTitle(), board.getMembers().get(0), board.getMembers().get(1)));
        return "index";
    }
}
