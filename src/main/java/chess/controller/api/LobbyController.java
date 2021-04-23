package chess.controller.api;

import chess.domain.game.ChessGame;
import chess.domain.piece.Color;
import chess.dto.DuplicateDTO;
import chess.dto.FinishDTO;
import chess.dto.RoomIdDTO;
import chess.dto.RoomListDTO;
import chess.dto.ScoreDTO;
import chess.dto.TitleDTO;
import chess.service.LobbyService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LobbyController {

    private final LobbyService lobbyService;

    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }


    @PostMapping("/lobby/new")
    public ResponseEntity newGame(@RequestBody @Valid TitleDTO titleDTO, BindingResult error) {
        if (error.hasErrors()) {
            return ResponseEntity.badRequest()
                .body(error.getAllErrors().get(0).getDefaultMessage());
        }
        return ResponseEntity.ok(new RoomIdDTO(lobbyService.newGame(titleDTO.getTitle())));
    }

    @PostMapping("/findRoomId")
    public ResponseEntity findRoomId(@RequestBody @Valid TitleDTO titleDTO, BindingResult error) {
        String title = titleDTO.getTitle().replaceAll("%20", " ");
        if (error.hasErrors()) {
            return ResponseEntity.badRequest()
                .body(error.getAllErrors().get(0).getDefaultMessage());
        }
        return ResponseEntity.ok(new RoomIdDTO(lobbyService.findRoomId(title)
            .orElseThrow(() -> new EmptyResultDataAccessException(0))));
    }

    @PostMapping("/isDuplicate")
    public ResponseEntity<DuplicateDTO> isDuplicate(@RequestBody TitleDTO titleDTO) {
        return ResponseEntity.ok(new DuplicateDTO(lobbyService.isDuplicate(titleDTO.getTitle())));
    }

    @GetMapping("/rooms")
    public ResponseEntity<RoomListDTO> findAllRooms() {
        return ResponseEntity.ok(new RoomListDTO(lobbyService.findAllRooms()));
    }

    @GetMapping("/finishByName/{roomName}")
    public ResponseEntity<FinishDTO> isFinished(@PathVariable String roomName) {
        return ResponseEntity.ok(new FinishDTO(lobbyService.isFinished(roomName)));
    }

    @GetMapping("/scoreByName/{roomName}")
    public ResponseEntity<ScoreDTO> score(@PathVariable String roomName) {
        ChessGame chessGame = lobbyService.loadGame(roomName);
        return ResponseEntity
            .ok(new ScoreDTO(chessGame.getScore(Color.BLACK), chessGame.getScore(Color.WHITE)));
    }
}
