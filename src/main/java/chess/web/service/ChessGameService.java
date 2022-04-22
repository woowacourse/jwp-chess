package chess.web.service;

import chess.domain.game.ChessGame;
import chess.domain.game.state.ChessBoard;
import chess.domain.game.state.Player;
import chess.domain.game.state.RunningGame;
import chess.domain.piece.Piece;
import chess.domain.piece.position.Position;
import chess.domain.piece.property.Color;
import chess.web.dao.ChessBoardDao;
import chess.web.dao.PlayerDao;
import chess.web.dto.MoveDto;
import chess.web.dto.MoveResultDto;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class ChessGameService {

    private final ChessBoardDao chessBoardDao;
    private final PlayerDao playerDao;

    public ChessGameService(ChessBoardDao chessBoardDao, PlayerDao playerDao) {
        this.chessBoardDao = chessBoardDao;
        this.playerDao = playerDao;
    }

    public void start() {
        ChessGame chessGame = new ChessGame();
        chessGame.start();

        removeAll();
        saveAll(chessGame);
    }

    public ChessBoard createChessBoard() {
        return ChessBoard.of(chessBoardDao.findAll());
    }

    private void removeAll() {
        chessBoardDao.deleteAll();
        playerDao.deleteAll();
    }

    private void saveAll(ChessGame chessGame) {
        Map<Position, Piece> chessBoard = chessGame.getBoard();
        for (Position position : chessBoard.keySet()) {
            chessBoardDao.save(position, chessBoard.get(position));
        }
        playerDao.save(Color.of(chessGame.getTurn()));
    }

    public Map<Position, Piece> findAllBoard() {
        return chessBoardDao.findAll();
    }

    public Player findTurn() {
        return playerDao.getPlayer();
    }

    public boolean isChessGameEnd(ChessGame chessGame) {
        return chessGame.isFinished();
    }

    public ChessGame getChessGame() {
        return ChessGame.of(new RunningGame(createChessBoard(), findTurn()));
    }

    public MoveResultDto move(MoveDto moveDto) {
        ChessGame chessGame = getChessGame();
        String turn = chessGame.getTurn();
        MoveResultDto moveResultDto = new MoveResultDto();

        try {
            chessGame.move(Position.of(moveDto.getSource()), Position.of(moveDto.getTarget()));
            removeAll();
            if (isChessGameEnd(chessGame)) {
                moveResultDto.setIsGameOver(true);
                moveResultDto.setWinner(turn);
                return moveResultDto;
            }
            saveAll(chessGame);
        } catch (IllegalArgumentException e) {
            moveResultDto.setIsMovable(false);
        }

        return moveResultDto;
    }

    public ModelAndView play() {
        Map<Position, Piece> board = findAllBoard();
        if (board.isEmpty()) {
            start();
        }

        ModelAndView modelAndView = new ModelAndView("index");
        for (Position position : board.keySet()) {
            Piece piece = board.get(position);
            modelAndView.addObject(position.toString(), piece);
        }

        modelAndView.addObject("turn",  findTurn().name());

        return modelAndView;
    }
}
