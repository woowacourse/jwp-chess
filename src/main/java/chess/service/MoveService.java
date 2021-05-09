package chess.service;

import chess.domain.board.piece.Square;
import chess.domain.board.position.Position;
import chess.domain.history.History;
import chess.domain.manager.Game;
import chess.domain.repository.board.SquareRepository;
import chess.domain.repository.game.GameRepository;
import chess.domain.repository.history.HistoryRepository;
import chess.service.dto.history.HistoryDto;
import chess.service.dto.move.MoveDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoveService {

    private final GameRepository gameRepository;
    private final SquareRepository squareRepository;
    private final HistoryRepository historyRepository;

    public MoveService(GameRepository gameRepository, SquareRepository squareRepository, HistoryRepository historyRepository) {
        this.gameRepository = gameRepository;
        this.squareRepository = squareRepository;
        this.historyRepository = historyRepository;
    }

    @Transactional
    public HistoryDto move(final MoveDto moveDto, Long gameId) {
        Game game = gameRepository.findById(gameId);
        Position source = Position.of(moveDto.getSource());
        Position target = Position.of(moveDto.getTarget());
        History history = new History(gameId, source, target, game.state());
        game.move(source, target);
        historyRepository.save(history);
        updatePiece(gameId, source, target, game);
        gameRepository.update(game);
        return HistoryDto.from(history);
    }

    private void updatePiece(Long gameId, Position source, Position target, Game game) {
        Square moveSquare = new Square(gameId, target, game.pickPiece(target));
        squareRepository.updateByGameIdAndPosition(moveSquare);
        Square emptySquare = new Square(gameId, source, game.pickPiece(source));
        squareRepository.updateByGameIdAndPosition(emptySquare);
    }
}
