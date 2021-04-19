package chess.service;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.Room;
import chess.domain.team.BlackTeam;
import chess.domain.team.WhiteTeam;
import chess.repository.ChessRepository;
import dto.ChessGameDto;
import dto.MoveDto;
import dto.RoomDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class ChessService {
    private final ChessRepository chessRepository;

    public ChessService(final ChessRepository chessRepository) {
        this.chessRepository = chessRepository;
    }

    public ResponseEntity<List<RoomDto>> loadAllRoom() {
        final List<RoomDto> rooms = chessRepository.loadAllRoom()
                .stream()
                .map(RoomDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(rooms);
    }

    public ResponseEntity<ChessGameDto> loadGame(Long roodId, Room room) {
        Room savedRoom = chessRepository.loadRoom(roodId);
        if (!savedRoom.checkPassword(room)) {
            return ResponseEntity.badRequest().body(null);
        }

        final ChessGame chessGame = chessRepository.loadGame(roodId);
        return ResponseEntity.ok().body(new ChessGameDto(chessGame));
    }

    public void createRoom(@RequestBody Room room) {
        chessRepository.createRoom(new ChessGame(new WhiteTeam(), new BlackTeam()), room);
    }

    public ResponseEntity<ChessGameDto> movePiece(Long roodId, MoveDto moveDto) {
        final ChessGame chessGame = chessRepository.loadGame(roodId);
        try {
            if (chessGame.move(Position.of(moveDto.getFrom()), Position.of(moveDto.getTo()))) {
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
