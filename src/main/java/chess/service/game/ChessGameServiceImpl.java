package chess.service.game;

import chess.domain.game.ChessGame;
import chess.domain.game.Position;
import chess.domain.game.ChessGameRepository;
import chess.domain.room.ChessRoomRepository;
import chess.domain.room.Room;
import dto.ChessGameDto;
import dto.MoveDto;
import org.springframework.stereotype.Service;

@Service
public class ChessGameServiceImpl implements ChessGameService {
    private final ChessRoomRepository chessRoomRepository;
    private final ChessGameRepository chessGameRepository;


    public ChessGameServiceImpl(final ChessRoomRepository chessRoomRepository, final ChessGameRepository chessGameRepository) {
        this.chessRoomRepository = chessRoomRepository;
        this.chessGameRepository = chessGameRepository;
    }

    @Override
    public ChessGameDto load(final Long roomId) {
        Room room = chessRoomRepository.room(roomId);
        Long gameId = room.getGameId();
        return new ChessGameDto(gameId, chessGameRepository.chessGame(gameId), room);
    }

    @Override
    public ChessGameDto move(final Long gameId, final MoveDto moveDto) {
        final ChessGame chessGame = chessGameRepository.chessGame(gameId);
        chessGame.move(Position.of(moveDto.getFrom()), Position.of(moveDto.getTo()));
        chessGameRepository.save(gameId, chessGame, moveDto);
        return new ChessGameDto(gameId, chessGame);
    }
}
