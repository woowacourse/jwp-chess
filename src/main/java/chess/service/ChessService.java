package chess.service;

import chess.dao.MoveDao;
import chess.domain.Game;
import chess.domain.board.Board;
import chess.domain.board.Path;
import chess.domain.piece.PieceColor;
import chess.domain.position.Position;
import chess.domain.state.Running;
import chess.dto.MoveRequest;
import chess.dto.ScoreDto;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChessService {

    private final MoveDao moveDao;

    public Game findGameById(int id) {
        Game game = new Game();
        game.changeState(new Running());
        List<MoveRequest> findGame = moveDao.getMovesById(id);
        findGame.forEach(move -> game.move(Position.of(move.getFrom()), Position.of(move.getTo())));
        return game;
    }

    public Board findBoardById(int id) {
        return findGameById(id).getBoard();
    }

    public boolean addMoveById(Position from, Position to, int id) {
        Board board = findBoardById(id);
        boolean movable = board.isMovable(to, board.generateAvailablePath(from));
        if (movable) {
            findGameById(id).move(from, to);
            moveDao.addMoveById(from, to, id);
        }
        return movable;
    }

    public Board restartBoardById(int id) {
        moveDao.deleteAllById(id);
        return findBoardById(id);
    }

    public boolean endGameById(int id) {
        return findGameById(id).isFinished();
    }

    public PieceColor findTurnById(int id) {
        return findGameById(id).getTurn();
    }

    public Map<PieceColor, Double> getScoresById(int id) {
        return new ScoreDto(findBoardById(id)).getScores();
    }

    public List<String> findPathById(Position from, int id) {
        Path path = findBoardById(id).generateAvailablePath(from);
        return path.positions()
            .stream()
            .map(Position::toString)
            .collect(Collectors.toList());
    }
}
