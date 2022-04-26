package chess.service;

import chess.dao.ChessGameDao;
import chess.dao.PieceDao;
import chess.domain.ChessGame;
import chess.domain.Command;
import chess.domain.piece.Team;
import chess.dto.ChessGameDto;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private final ChessGameDao chessGameDao;
    private final PieceDao pieceDao;

    public ChessService(ChessGameDao chessGameDao, PieceDao pieceDao) {
        this.chessGameDao = chessGameDao;
        this.pieceDao = pieceDao;
    }

    public List<String> getCurrentChessBoard(String gameName) {
        ChessGame chessGame = chessGameDao.findByName(gameName);
        return chessGame.getChessBoardSymbol();
    }

    public List<String> move(String gameName, String moveCommand) {
        ChessGame chessGame = chessGameDao.findByName(gameName);
        Command command = Command.from(moveCommand);

        chessGame.progress(command);
        save(chessGame);

        return chessGame.getChessBoardSymbol();
    }

    public Map<Team, Double> getScore(String gameName) {
        ChessGame chessGame = chessGameDao.findByName(gameName);
        return chessGame.calculateResult();
    }

    public String finish(String gameName, Command command) {
        ChessGame chessGame = chessGameDao.findByName(gameName);

        chessGame.progress(command);
        save(chessGame);

        return chessGame.getWinTeamName();
    }

    public List<String> findByName(String gameName) throws IllegalStateException {
        ChessGame chessGame = chessGameDao.findByName(gameName);

        if (chessGame == null) {
            return createChessBoard(gameName);
        }

        return chessGame.getChessBoardSymbol();
    }

    private List<String> createChessBoard(String gameName) {
        ChessGame chessGame = new ChessGame(gameName);

        chessGame.progress(Command.from("start"));
        save(chessGame);

        return chessGame.getChessBoardSymbol();
    }

    public void save(ChessGame chessGame) throws IllegalStateException {
        String gameName = chessGame.getGameName();
        ChessGame chessGameByName = chessGameDao.findByName(gameName);
        ChessGameDto chessGameDto = ChessGameDto.from(chessGame);

        if (chessGameByName == null) {
            saveChessGame(chessGameDto);
            return;
        }

        updateChessGame(chessGameDto);
    }

    private void updateChessGame(ChessGameDto chessGameDto) throws IllegalStateException {
        chessGameDao.update(chessGameDto);
        pieceDao.update(chessGameDto);
    }

    private void saveChessGame(ChessGameDto chessGameDto) throws IllegalStateException {
        chessGameDao.save(chessGameDto);
        pieceDao.save(chessGameDto);
    }

    public boolean isEnd(String gameName) {
        ChessGame chessGame = chessGameDao.findByName(gameName);
        return chessGame.isEnd();
    }
}
