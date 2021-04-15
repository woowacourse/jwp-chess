package chess.controller;

import chess.dao.GameDao;
import chess.domain.ChessGame;
import chess.domain.Room;
import chess.domain.team.BlackTeam;
import chess.domain.team.WhiteTeam;
import chess.repository.ChessRepository;
import dto.ChessGameDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SpringChessController {
    @Autowired
    ChessRepository chessRepository;

    @GetMapping("/room")
    public ResponseEntity<List<Room>> loadAllRoom() {
        return ResponseEntity.ok().body(chessRepository.loadAllRoom());
    }


    @PostMapping("/room/{id}")
    public ResponseEntity<ChessGameDto> loadGame(@PathVariable("id") Long roodId, @RequestBody Room room) {
        // 비밀번호 검증 성공
        final ChessGame chessGame = chessRepository.loadGame(roodId);
        return ResponseEntity.ok().body(new ChessGameDto(chessGame));
        // 실패하면 400 배드 리퀘스트
    }



    @PostMapping("/room")
    public void createRoom(@RequestBody Room room) {
        chessRepository.createRoom(new ChessGame(new WhiteTeam(), new BlackTeam()), room);
    }


}
