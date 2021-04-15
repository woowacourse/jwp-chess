package chess.controller;

import chess.domain.ChessGame;
import chess.domain.Room;
import chess.domain.team.BlackTeam;
import chess.domain.team.WhiteTeam;
import chess.repository.ChessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpringChessController {
    @Autowired
    ChessRepository chessRepository;

    @GetMapping("/room")
    public ResponseEntity<List<Room>> loadAllRoom() {
        return ResponseEntity.ok().body(chessRepository.loadAllRoom());
    }


    @PostMapping("/room")
    public void createRoom(@RequestBody Room room) {
        chessRepository.createRoom(new ChessGame(new BlackTeam(), new WhiteTeam()), room);
    }


}
