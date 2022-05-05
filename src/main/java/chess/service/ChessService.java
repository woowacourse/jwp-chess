package chess.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import chess.dao.ChessGameDao;
import chess.dao.PieceDao;
import chess.domain.ChessGame;
import chess.domain.Command;
import chess.domain.piece.Piece;
import chess.domain.piece.Team;
import chess.domain.position.Position;
import chess.dto.ChessGameDto;
import chess.dto.PieceDto;
import chess.exception.DeleteProgressGameException;
import chess.exception.PasswordNotMatchedException;

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

    public void delete(String password, int chessGameId) {
        ChessGame chessGame = chessGameDao.findById(chessGameId);

        if (chessGame.isProgress()) {
            throw new DeleteProgressGameException("게임이 진행중 입니다.");
        }

        if (!chessGame.matchPassword(password)) {
            throw new PasswordNotMatchedException("패스워드가 틀립니다.");
        }

        chessGameDao.delete(chessGameId);
    }

    public int save(String gameName, String password) {
        int savedId = chessGameDao.save(gameName, password);
        ChessGame chessGame = new ChessGame(gameName);
        createChessBoard(chessGame, savedId);
        return savedId;
    }

    public void start(int chessGameId) {
        ChessGame chessGame = new ChessGame();
        createChessBoard(chessGame, chessGameId);
        chessGameDao.update(chessGame.getState().getTurn(), chessGameId);
    }

    private void createChessBoard(ChessGame chessGame, int chessGameId) {
        chessGame.progress(Command.from("start"));
        ChessGameDto chessGameDto = ChessGameDto.from(chessGame);
        pieceDao.save(chessGameDto, chessGameId);
    }

    public void move(String from, String to, int chessGameId) {
        ChessGame chessGame = chessGameDao.findById(chessGameId);
        Command command = Command.from(makeCommand(from, to));
        chessGame.progress(command);
        moveUpdateDB(from, to, chessGameId, chessGame);
    }

    private void moveUpdateDB(String from, String to, int chessGameId, ChessGame chessGame) {
        pieceDao.deleteByPosition(to, chessGameId);
        pieceDao.updatePosition(from, to, chessGameId);
        chessGameDao.update(chessGame.getState().getTurn(), chessGameId);
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
        chessGameDao.update("end", chessGameId);
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

    public List<PieceDto> getPieces(int chessGameId) {
        ChessGame chessGame = chessGameDao.findById(chessGameId);
        Map<Position, Piece> cells = chessGame.getCells();
        return PieceDto.getOnBoard(cells);
    }
}
