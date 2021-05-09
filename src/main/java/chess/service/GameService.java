package chess.service;

import chess.domain.board.piece.Square;
import chess.domain.manager.Game;
import chess.domain.repository.board.SquareRepository;
import chess.domain.repository.game.GameRepository;
import chess.service.dto.game.GameDto;
import chess.service.dto.piece.PieceDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final SquareRepository squareRepository;

    public GameService(GameRepository gameRepository, SquareRepository squareRepository) {
        this.gameRepository = gameRepository;
        this.squareRepository = squareRepository;
    }

    @Transactional
    public Long saveNewGame() {
        Game game = new Game();
        Long gameId = gameRepository.save(game);
        squareRepository.saveBoard(gameId, game.getBoard());
        return gameId;
    }

    public List<PieceDto> findPiecesById(final Long gameId) {
        List<Square> squares = squareRepository.findByGameId(gameId);
        return squares.stream()
                .map(square -> new PieceDto(square.getGameId(), square.getPosition().parseString(), square.getPiece().getSymbol()))
                .collect(Collectors.toList());
    }

    public GameDto findById(final Long gameId) {
        Game game = gameRepository.findById(gameId);
        return GameDto.from(game);
    }

    public void update(Game game) {
        gameRepository.update(game);
    }
}
