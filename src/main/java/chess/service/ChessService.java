package chess.service;

import chess.dao.ChessGameDao;
import chess.dao.PieceDao;
import chess.domain.ChessGame;
import chess.domain.Command;
import chess.domain.piece.Team;
import chess.domain.state.State;
import chess.dto.ChessGameDto;
import chess.exception.IllegalDeleteException;
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

    public List<String> getCurrentChessBoard(Long id) {
        ChessGame chessGame = chessGameDao.findById(id);
        return chessGame.getChessBoardSymbol();
    }

    public Long save(String gameName, String password) {
        ChessGame chessGame = new ChessGame(gameName);
        chessGame.progress(Command.from("start"));
        ChessGameDto chessGameDto = ChessGameDto.from(chessGame);

        Long id = chessGameDao.save(chessGameDto, password);
        pieceDao.save(id, chessGameDto);

        return id;
    }

    public List<String> move(Long id, String moveCommand) {
        ChessGame chessGame = chessGameDao.findById(id);
        Command command = Command.from(moveCommand);

        chessGame.progress(command);
        updateChessGame(id, ChessGameDto.from(chessGame));

        return chessGame.getChessBoardSymbol();
    }

    private void updateChessGame(Long id, ChessGameDto chessGameDto) throws IllegalStateException {
        chessGameDao.update(id, chessGameDto);
        pieceDao.update(id, chessGameDto);
    }

    public Map<Team, Double> getScore(Long id) {
        ChessGame chessGame = chessGameDao.findById(id);
        return chessGame.calculateResult();
    }

    public String finish(Long id, Command command) {
        ChessGame chessGame = chessGameDao.findById(id);

        chessGame.progress(command);
        updateChessGame(id, ChessGameDto.from(chessGame));

        return chessGame.getWinTeamName();
    }

    public List<String> findChessBoardById(Long id) throws IllegalStateException {
        ChessGame chessGame = chessGameDao.findById(id);

        return chessGame.getChessBoardSymbol();
    }

    public boolean isEnd(Long id) {
        ChessGame chessGame = chessGameDao.findById(id);
        return chessGame.isEnd();
    }

    public List<String> findAllGameName() {
        return chessGameDao.findAllGameName();
    }

    public Long findIdByGameNameAndPassword(String gameName, String password) {
        return chessGameDao.findIdByGameNameAndPassword(gameName,password);
    }

    public int deleteByGameNameAndPassword(State state, String gameName, String password) {
        if (!state.isEnd()) {
            throw new IllegalDeleteException();
        }
        return chessGameDao.deleteByGameNameAndPassword(gameName, password);
    }

    public State findStateByGameNameAndPassword(String gameName, String password) {
        return chessGameDao.findStateByGameNameAndPassword(gameName, password);
    }
}
