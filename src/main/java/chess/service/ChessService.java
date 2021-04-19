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
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private final ChessRepository chessRepository;

    public ChessService(final ChessRepository chessRepository) {
        this.chessRepository = chessRepository;
    }

    public List<RoomDto> loadAllRoom() {
        return chessRepository.loadAllRoom()
            .stream()
            .map(RoomDto::new)
            .collect(Collectors.toList());
    }

    public ChessGameDto loadGame(long roodId, Room room) {
        Room savedRoom = chessRepository.loadRoom(roodId);
        if (!savedRoom.checkPassword(room)) {
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }

        return new ChessGameDto(chessRepository.loadGame(roodId));
    }

    public void createRoom(Room room) {
        chessRepository.createRoom(new ChessGame(new WhiteTeam(), new BlackTeam()), room);
    }

    public ChessGameDto movePiece(long roodId, MoveDto moveDto) {
        final ChessGame chessGame = chessRepository.loadGame(roodId);
        chessGame.move(Position.of(moveDto.getFrom()), Position.of(moveDto.getTo()));
        chessRepository.saveGame(roodId, chessGame, moveDto);
        return new ChessGameDto(chessGame);
    }
}
