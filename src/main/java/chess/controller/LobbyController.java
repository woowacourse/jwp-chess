package chess.controller;

import chess.domain.game.ChessGame;
import chess.domain.piece.Color;
import chess.dto.*;
import chess.service.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class LobbyController {
    private final LobbyService lobbyService;

    @Autowired
    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @GetMapping("/")
    public String lobby() {
        return "lobby";
    }

    @PostMapping("/lobby/new")
    @ResponseBody
    public ResponseEntity newGame(@RequestBody @Valid TitleDTO titleDTO, BindingResult error) {
        if (error.hasErrors()) {
            return ResponseEntity.badRequest().body(error.getAllErrors().get(0).getDefaultMessage());
        }
        return ResponseEntity.ok(new RoomIdDTO(lobbyService.newGame(titleDTO.getTitle())));
    }

    @PostMapping("/findRoomId")
    @ResponseBody
    public ResponseEntity findRoomId(@RequestBody @Valid TitleDTO titleDTO, BindingResult error) {
        if (error.hasErrors()) {
            return ResponseEntity.badRequest().body(error.getAllErrors().get(0).getDefaultMessage());
        }
        String find =
                lobbyService.findRoomId(titleDTO.getTitle()).orElseThrow(() -> new EmptyResultDataAccessException(0));
        return ResponseEntity.ok(new RoomIdDTO(find));
    }

    @PostMapping("/isDuplicate")
    @ResponseBody
    public ResponseEntity isDuplicate(@RequestBody TitleDTO titleDTO) {
        return ResponseEntity.ok(new DuplicateDTO(lobbyService.isDuplicate(titleDTO.getTitle())));
    }

    @GetMapping("/rooms")
    @ResponseBody
    public ResponseEntity findAllRooms() {
        return ResponseEntity.ok(new RoomListDTO(lobbyService.findAllRooms()));
    }

    @GetMapping("/finishByName/{roomName}")
    @ResponseBody
    public ResponseEntity isFinished(@PathVariable String roomName) {
        return ResponseEntity.ok(new FinishDTO(lobbyService.isFinished(roomName)));
    }

    @GetMapping("/scoreByName/{roomName}")
    @ResponseBody
    public ResponseEntity score(@PathVariable String roomName) {
        ChessGame chessGame = lobbyService.loadGame(roomName);
        return ResponseEntity.ok(new ScoreDTO(chessGame.getScore(Color.BLACK), chessGame.getScore(Color.WHITE)));
    }
}
