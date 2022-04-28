package chess.controller;

import chess.entities.Member;
import chess.domain.pieces.Color;
import chess.domain.pieces.Piece;
import chess.domain.position.Position;
import chess.dto.response.BoardDto;
import chess.dto.response.GameStatusDto;
import chess.dto.request.MoveDto;
import chess.dto.request.NewGameInfoDto;
import chess.dto.response.RoomDto;
import chess.dto.response.RoomsDto;
import chess.dto.response.StatusDto;
import chess.entities.ChessGame;
import chess.service.GameService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChessController {

    private final GameService gameService;

    public ChessController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<RoomDto> boardsDto = new ArrayList<>();
        List<ChessGame> boards = gameService.findAllBoard();
        for (ChessGame board : boards) {
            boardsDto.add(new RoomDto(board.getId(), board.getRoomTitle(), board.getMembers().get(0),
                    board.getMembers().get(1)));
        }
        model.addAttribute("boards", new RoomsDto(boardsDto));
        return "home";
    }

    @PostMapping("/room")
    public String createRoom(@ModelAttribute NewGameInfoDto newGameInfoDto) {
        final ChessGame board = new ChessGame(newGameInfoDto.getTitle(), Color.WHITE,
                List.of(new Member(newGameInfoDto.getFirstMemberName()),
                        new Member(newGameInfoDto.getSecondMemberName())),
                newGameInfoDto.getPassword());
        int roomId = gameService.createBoard(board).getId();
        return "redirect:/room/" + roomId;
    }

    @GetMapping("/room/{roomId}")
    public String showRoom(@PathVariable("roomId") int id, Model model) {
        model.addAttribute("roomId", id);
        ChessGame board = gameService.getBoard(id);
        Map<String, Piece> pieces = gameService.getPieces(id);
        model.addAttribute("board",
                BoardDto.of(pieces, board.getRoomTitle(), board.getMembers().get(0), board.getMembers().get(1)));
        return "index";
    }

    @ResponseBody
    @PostMapping("/room/{roomId}/move")
    public ResponseEntity<GameStatusDto> movePiece(@PathVariable("roomId") int roomId, @RequestBody MoveDto moveDto) {
        if (gameService.isEnd(roomId)) {
            throw new IllegalArgumentException("게임이 이미 끝났다.");
        }
        gameService.move(roomId, Position.of(moveDto.getStart()), Position.of(moveDto.getTarget()));
        return ResponseEntity.ok(new GameStatusDto(gameService.isEnd(roomId)));
    }

    @PostMapping("/room/{roomId}/end")
    public ResponseEntity<Void> endGame(@PathVariable("roomId") int id, @RequestParam("password") String password) {
        System.out.println("password" + password);
        if (!gameService.isEnd(id)) {
            throw new IllegalArgumentException("진행중인 게임은 삭제할 수 없어~");
        }
        if (!gameService.end(id, password)) {
            throw new IllegalArgumentException("게임 삭제에 실패하였습니다.");
        }
        return ResponseEntity.ok(null);
    }

    @ResponseBody
    @GetMapping("/room/{roomId}/status")
    public StatusDto showStatus(@PathVariable("roomId") int id) {
        return gameService.status(id);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleEmailDuplicateException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
