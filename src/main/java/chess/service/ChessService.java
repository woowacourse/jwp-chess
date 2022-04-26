package chess.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import chess.dao.ChessGameDao;
import chess.dao.PieceDao;
import chess.domain.ChessGame;
import chess.domain.Command;
import chess.domain.piece.Team;
import chess.dto.ChessGameDto;

@Service
public class ChessService {

    private final ChessGameDao chessGameDao;
    private final PieceDao pieceDao;

    public ChessService(ChessGameDao chessGameDao, PieceDao pieceDao) {
        this.chessGameDao = chessGameDao;
        this.pieceDao = pieceDao;
    }

    public List<ChessGameDto> findAllChessGames() {
        return chessGameDao.findAllChessGames();
    }

    public void delete(int chessGameId) {
        chessGameDao.delete(chessGameId);
    }

    public void save(String gameName, String password) {
        int savedId = chessGameDao.save(gameName, password);
        createChessBoard(gameName, savedId);
    }

    private void createChessBoard(String gameName, int chessGameId) {
        ChessGame chessGame = new ChessGame(gameName);
        chessGame.progress(Command.from("start"));
        ChessGameDto chessGameDto = ChessGameDto.from(chessGame);
        pieceDao.save(chessGameDto, chessGameId);
    }

    public List<String> getCurrentChessBoard(int chessGameId) {
        ChessGame chessGame = chessGameDao.findById(chessGameId);
        return chessGame.getChessBoardSymbol();
    }

    public List<String> move(String from, String to, int chessGameId) {
        ChessGame chessGame = chessGameDao.findById(chessGameId);
        Command command = Command.from(makeCommand(from, to));
        chessGame.progress(command);
        pieceDao.deleteByPosition(to, chessGameId);
        pieceDao.updatePosition(from, to, chessGameId);
        chessGameDao.update(chessGame.getState().getTurn(), chessGameId);
        return chessGame.getChessBoardSymbol();
    }

    private String makeCommand(String from, String to) {
        return "move " + from + " " + to;
    }

    public Map<Team, Double> getScore(int chessGameId) {
        ChessGame chessGame = chessGameDao.findById(chessGameId);
        return chessGame.calculateResult();
    }

    public String finish(Command command, int chessGameId) {
        ChessGame chessGame = chessGameDao.findById(chessGameId);
        chessGame.progress(command);
        return chessGame.getWinTeamName();
    }

    public List<String> findChessBoardById(int chessGameId) throws IllegalStateException {
        ChessGame chessGame = chessGameDao.findById(chessGameId);
        return chessGame.getChessBoardSymbol();
    }
    public boolean isEnd(int chessGameId) {
        ChessGame chessGame = chessGameDao.findById(chessGameId);
        return chessGame.isEnd();
    }
}
