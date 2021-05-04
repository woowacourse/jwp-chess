package chess.service.game;

import chess.domain.game.ChessGame;
import chess.domain.game.ChessGameRepository;
import chess.domain.game.Position;
import chess.dto.ChessGameDto;
import chess.dto.request.GameMoveRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChessGameServiceImpl implements ChessGameService {
    private final ChessGameRepository chessGameRepository;

    @Autowired
    public ChessGameServiceImpl(final ChessGameRepository chessGameRepository) {
        this.chessGameRepository = chessGameRepository;
    }

    @Override
    public ChessGameDto move(final Long id, final GameMoveRequest moveDto) {
        final ChessGame chessGame = chessGameRepository.chessGame(id);
        chessGame.move(Position.of(moveDto.getFrom()), Position.of(moveDto.getTo()));
        chessGameRepository.save(id, chessGame, moveDto);
        return new ChessGameDto(id, chessGame);
    }
}
