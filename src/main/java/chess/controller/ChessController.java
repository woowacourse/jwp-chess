package chess.controller;

import chess.domain.game.BoardInitializer;
import chess.domain.game.ChessBoard;
import chess.domain.member.Member;
import chess.domain.pieces.Color;
import chess.domain.position.Position;
import chess.dto.GameStatusDto;
import chess.dto.MoveForm;
import chess.dto.RequestDto;
import chess.dto.StatusDto;
import chess.service.GameService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
public class ChessController {

    private final GameService gameService;

    @Value("home")
    private String mainView;

    @Value("index")
    private String roomView;

    public ChessController(GameService gameService) {
        this.gameService = gameService;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleEmailDuplicateException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("boards", gameService.getRooms());
        return mainView;
    }

    @PostMapping("/room")
    public String createRoom(@ModelAttribute RequestDto requestDto) {
        final ChessBoard board = new ChessBoard(requestDto.getTitle(), Color.WHITE,
                List.of(new Member(requestDto.getFirstMemberName()), new Member(requestDto.getSecondMemberName())), requestDto.getPassword());
        int roomId = gameService.saveBoard(board, new BoardInitializer()).getId();
        return "redirect:/room/" + roomId;
    }

    @GetMapping("/room/{roomId}")
    public String showRoom(@PathVariable("roomId") int id, Model model) {
        model.addAttribute("roomId", id);
        model.addAttribute("board", gameService.getBoard(id));
        return roomView;
    }

    @ResponseBody
    @PatchMapping("/room/{roomId}/move")
    public ResponseEntity<GameStatusDto> moveByCommand(@PathVariable("roomId") int id, @RequestBody MoveForm moveForm) {
        try {
            gameService.move(id, Position.of(moveForm.getSource()), Position.of(moveForm.getTarget()));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        return new ResponseEntity<>(new GameStatusDto(gameService.isEnd(id)), HttpStatus.OK);
    }

    @PostMapping("/room/{roomId}/end")
    public ResponseEntity<Void> endGame(@PathVariable("roomId") int id, @RequestParam("password") String password) {
        if (!gameService.isEnd(id)) {
            throw new IllegalArgumentException("진행중인 게임은 삭제할 수 없습니다.");
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
}
