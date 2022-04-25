package chess.controller;

import chess.domain.game.BoardInitializer;
import chess.domain.game.ChessBoard;
import chess.domain.member.Member;
import chess.domain.pieces.Color;
import chess.domain.position.Position;
import chess.dto.RequestDto;
import chess.dto.GameStatusDto;
import chess.dto.StatusDto;
import chess.mapper.Command;
import chess.service.GameService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@Controller
public class ChessController {

    private static final String MOVE_DELIMITER = " ";
    private static final int MOVE_COMMAND_SIZE = 3;
    private static final int SOURCE_INDEX = 1;
    private static final int TARGET_INDEX = 2;

    private final GameService gameService;

    @Value("home")
    private String mainView;

    @Value("index")
    private String roomView;

    @Value("result")
    private String resultView;

    public ChessController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("boards", gameService.getRooms());
        return mainView;
    }

    @PostMapping("/room")
    public String createRoom(@ModelAttribute RequestDto requestDto) {
        final ChessBoard board = new ChessBoard(requestDto.getTitle(), Color.WHITE,
                List.of(new Member(requestDto.getFirstMemberName()), new Member(requestDto.getSecondMemberName())));
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
    @PostMapping("/room/{roomId}/move")
    public ResponseEntity<GameStatusDto> moveByCommand(@PathVariable("roomId") int id, @RequestBody String body) {
        final String[] split = body.split("=");
        if (Command.isMove(split[1])) {
            moveByCommand(id, split);
        }
        return new ResponseEntity<>(new GameStatusDto(gameService.isEnd(id)), HttpStatus.OK);
    }

    private void moveByCommand(int id, String[] split) {
        try {
            move(id, Arrays.asList(split[1].split(MOVE_DELIMITER)));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    private void move(int roomId, final List<String> commands) {
        if (commands.size() == MOVE_COMMAND_SIZE) {
            gameService.move(roomId, Position.of(commands.get(SOURCE_INDEX)), Position.of(commands.get(TARGET_INDEX)));
        }
    }

    @PostMapping("/room/{roomId}/end")
    public String endGame(@PathVariable("roomId") int id, Model model) {
        model.addAttribute("result", gameService.status(id));
        gameService.end(id);
        return resultView;
    }

    @ResponseBody
    @GetMapping("/room/{roomId}/status")
    public StatusDto showStatus(@PathVariable("roomId") int id) {
        return gameService.status(id);
    }
}
