package chess.controller;

import chess.domain.ChessGame;
import chess.domain.Room;
import chess.domain.team.BlackTeam;
import chess.domain.team.WhiteTeam;
import chess.repository.ChessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringChessController {
    @Autowired
    ChessRepository chessRepository;
    @PostMapping("/room")
    public void createRoom(@RequestBody Room room) {
        chessRepository.createRoom(new ChessGame(new BlackTeam(), new WhiteTeam()), room);
    }
}
