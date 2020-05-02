package wooteco.chess.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import wooteco.chess.db.entity.PlayerEntity;
import wooteco.chess.dto.req.PlayersDto;
import wooteco.chess.dto.res.RoomDto;
import wooteco.chess.service.RoomService;
import wooteco.chess.service.SpringGameService;
import wooteco.chess.service.SpringPlayerService;

@Controller
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;
    private final SpringPlayerService playerService;
    private final SpringGameService boardService;

    public RoomController(RoomService roomService,
        SpringPlayerService playerService, SpringGameService boardService) {
        this.roomService = roomService;
        this.playerService = playerService;
        this.boardService = boardService;
    }

    @GetMapping
    public String createNewGame() {
        return "createNewGame";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Object> createNewGame(@RequestBody PlayersDto playersDto) {
        try {
            long roomId = createRoom(playersDto);
            boardService.createBoard(roomId);
            return ResponseEntity
                .status(200)
                .body(
                    new RoomDto(roomId, playersDto.getPlayer1Name(), playersDto.getPlayer2Name()));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public String start(@PathVariable int id, Model model) {
        model.addAttribute("id", id);
        return "play";
    }

    private long createRoom(PlayersDto playersDto) {
        long player1Id = playerService.save(
            new PlayerEntity(playersDto.getPlayer1Name(), playersDto.getPlayer1Password(),
                "white"));
        long player2Id = playerService.save(
            new PlayerEntity(playersDto.getPlayer2Name(), playersDto.getPlayer2Password(),
                "black"));
        return boardService.createRoom(player1Id, player2Id);
    }

    @GetMapping("/games")
    @ResponseBody
    public ResponseEntity<Object> findEveryRoom() {
        try {
            final List<RoomDto> everyRoom = roomService.findAll();
            return ResponseEntity.status(200).body(everyRoom);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
