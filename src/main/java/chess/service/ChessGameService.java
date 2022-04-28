package chess.service;

import chess.chessgame.ChessGame;
import chess.chessgame.MovingPosition;
import chess.repository.ChessboardRepository;
import chess.dto.ScoreDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChessGameService {

    private final ChessboardRepository dao;
    private ChessGame chessGame = new ChessGame();

    public ChessGameService(ChessboardRepository dao) {
        this.dao = dao;
    }

    public void init() {
        if (dao.isDataExist()) {
            chessGame = dao.find();
            return;
        }
        chessGame = new ChessGame();
    }

    public void start() {
        chessGame.start();
    }

    public void restart() {
        dao.truncateAll();
        chessGame = new ChessGame();
    }

    public void move(String from, String to) {
        chessGame.move(new MovingPosition(from, to));
    }

    public ScoreDto status() {
        return chessGame.computeScore();
    }

    public void save() {
        dao.save(chessGame);
    }

    public void end() {
        chessGame.end();
    }

    public List<String> getPiecesByUnicode() {
        return chessGame.getPiecesByUnicode();
    }

    public ChessGame getChessGame() {
        return chessGame;
    }

}
