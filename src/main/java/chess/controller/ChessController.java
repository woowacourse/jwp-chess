package chess.controller;

import chess.dto.GameStatusDto;
import chess.dto.MoveDto;
import chess.dto.NewGameInfoDto;
import chess.dto.StatusDto;
import chess.service.GameService;
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
        model.addAttribute("boards", gameService.getRooms());
        return "home";
    }

    @PostMapping("/room")
    public String createRoom(@ModelAttribute NewGameInfoDto newGameInfoDto) {
        int roomId = gameService.createBoard(newGameInfoDto).getId();
        return "redirect:/room/" + roomId;
    }

    @GetMapping("/room/{roomId}")
    public String showRoom(@PathVariable("roomId") int id, Model model) {
        model.addAttribute("roomId", id);
        model.addAttribute("board", gameService.getBoard(id));
        return "index";
    }

    @ResponseBody
    @PostMapping("/room/{roomId}/move")
    public ResponseEntity<GameStatusDto> movePiece(@PathVariable("roomId") int id, @RequestBody MoveDto moveDto) {
        if (gameService.isEnd(id)) {
            throw new IllegalArgumentException("게임이 이미 끝났다.");
        }
        gameService.move(id, moveDto.getStart(), moveDto.getTarget());
        return ResponseEntity.ok(new GameStatusDto(gameService.isEnd(id)));
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
