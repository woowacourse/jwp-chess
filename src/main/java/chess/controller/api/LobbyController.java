package chess.controller.api;

import chess.domain.game.ChessGame;
import chess.domain.piece.Color;
import chess.dto.DuplicateDto;
import chess.dto.FinishDto;
import chess.dto.RoomIdDto;
import chess.dto.RoomListDto;
import chess.dto.ScoreDto;
import chess.dto.TitleDto;
import chess.service.LobbyService;
import javax.validation.Valid;

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
    public ResponseEntity newGame(@RequestBody @Valid TitleDto titleDTO) {
        return ResponseEntity.ok(new RoomIdDto(lobbyService.newGame(titleDTO.getTitle())));
    }

    @PostMapping("/findRoomId")
    public ResponseEntity findRoomId(@RequestBody @Valid TitleDto titleDTO) {
        String title = titleDTO.getTitle().replaceAll("%20", " ");
        return ResponseEntity.ok(new RoomIdDto(lobbyService.findRoomId(title)
            .orElseThrow(() -> new EmptyResultDataAccessException(0))));
    }

    @PostMapping("/isDuplicate")
    public ResponseEntity<DuplicateDto> isDuplicate(@RequestBody TitleDto titleDTO) {
        return ResponseEntity.ok(new DuplicateDto(lobbyService.isDuplicate(titleDTO.getTitle())));
    }

    @GetMapping("/rooms")
    public ResponseEntity<RoomListDto> findAllRooms() {
        return ResponseEntity.ok(new RoomListDto(lobbyService.findAllRooms()));
    }

    @GetMapping("/finishByName/{roomName}")
    public ResponseEntity<FinishDto> isFinished(@PathVariable String roomName) {
        return ResponseEntity.ok(new FinishDto(lobbyService.isFinished(roomName)));
    }

    @GetMapping("/scoreByName/{roomName}")
    public ResponseEntity<ScoreDto> score(@PathVariable String roomName) {
        ChessGame chessGame = lobbyService.loadGame(roomName);
        return ResponseEntity
            .ok(new ScoreDto(chessGame.getScore(Color.BLACK), chessGame.getScore(Color.WHITE)));
    }
}
