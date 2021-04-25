package chess.service.game;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.ChessGameRepository;
import dto.ChessGameDto;
import dto.MoveDto;
import org.springframework.stereotype.Service;

@Service
public class ChessGameServiceImpl implements ChessGameService {
    private final ChessGameRepository chessGameRepository;

    public ChessGameServiceImpl(final ChessGameRepository chessGameRepository) {
        this.chessGameRepository = chessGameRepository;
    }

    @Override
    public ChessGameDto move(final Long gameId, final MoveDto moveDto) {
        final ChessGame chessGame = chessGameRepository.chessGame(gameId);
        chessGame.move(Position.of(moveDto.getFrom()), Position.of(moveDto.getTo()));
        chessGameRepository.save(gameId, chessGame, moveDto);
        return new ChessGameDto(gameId, chessGame);
    }
}
