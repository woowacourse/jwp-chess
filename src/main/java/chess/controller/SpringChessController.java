package chess.controller;

import chess.dao.GameDao;
import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.Room;
import chess.domain.team.BlackTeam;
import chess.domain.team.WhiteTeam;
import chess.repository.ChessRepository;
import dto.ChessGameDto;
import dto.MoveDto;
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

    @PutMapping("/room/{id}")
    public ResponseEntity<ChessGameDto> movePiece(@PathVariable("id") Long roodId, @RequestBody MoveDto moveDto) {
        final ChessGame chessGame = chessRepository.loadGame(roodId);
        try {
            if (chessGame.move(Position.of(moveDto.getFrom()), Position.of(moveDto.getTo()))) {
                // 세이브 로직
                chessRepository.saveGame(roodId, chessGame, moveDto);
                return ResponseEntity.ok().body(new ChessGameDto(chessGame));
            }
            throw new IllegalArgumentException("이동할 수 없습니다.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
}
