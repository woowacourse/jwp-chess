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
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private final MoveDao moveDao;

    public ChessService(MoveDao moveDao) {
        this.moveDao = moveDao;
    }

    public Game findGame() {
        Game game = new Game();
        game.changeState(new Running());
        List<MoveRequest> findGame = moveDao.getMoves();
        findGame.forEach(move -> game.move(move.getFrom(), move.getTo()));
        return game;
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
