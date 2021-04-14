package chess.spring.service;

import chess.Dto.MoveRequest;
import chess.spring.dao.MoveDao;
import chess.Dto.ScoreDto;
import chess.domain.Game;
import chess.domain.board.Board;
import chess.domain.board.Path;
import chess.domain.piece.PieceColor;
import chess.domain.position.Position;
import chess.domain.state.Running;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class SpringChessService {
    private final MoveDao moveDao;

    public SpringChessService(MoveDao moveDao) {
        this.moveDao = moveDao;
    }

    public Game findGame() {
        Game game = new Game();
        game.changeState(new Running());
        Map<Position, Position> moves = mapPositions(moveDao.getMoves());
        moves.forEach(game::move);
        return game;
    }

    private Map<Position, Position> mapPositions(List<MoveRequest> moveRequests) {
        return moveRequests.stream()
            .collect(Collectors.toMap(MoveRequest::getFrom, MoveRequest::getTo));
    }

    public Board findBoard() {
        return findGame().getBoard();
    }

    public boolean addMove(Position from, Position to) {
        Board board = findBoard();
        boolean movable = board.isMovable(to, board.generateAvailablePath(from));
        if (movable) {
            findGame().move(from, to);
            moveDao.addMove(from, to);
        }
        return movable;
    }

    public Board restartBoard() {
        moveDao.deleteAll();
        return findBoard();
    }

    public boolean endGame() {
        return findGame().isFinished();
    }

    public PieceColor findTurn() {
        return findGame().getTurn();
    }

    public Map<PieceColor, Double> getScores() {
        return new ScoreDto(findBoard()).getScores();
    }

    public List<String> findPath(Position from) {
        Path path = findBoard().generateAvailablePath(from);
        return path.positions()
            .stream()
            .map(Position::toString)
            .collect(Collectors.toList());
    }
}
