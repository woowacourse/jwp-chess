package chess.repository;

import chess.dao.ChessDAO;
import chess.domain.ChessRepository;
import chess.domain.game.ChessGame;
import org.springframework.stereotype.Repository;

@Repository
public class ChessRepositoryImpl implements ChessRepository {

    private final ChessDAO chessDAO;

    public ChessRepositoryImpl(ChessDAO chessDAO) {
        this.chessDAO = chessDAO;
    }

    public void save(ChessGame chessGame) {
        chessDAO.addChessGame(
                chessGame.getGameId(),
                ChessGameConvertor.chessGameToJson(chessGame)
        );
    }

    public void update(ChessGame chessGame) {
        chessDAO.updateChessGame(
                chessGame.getGameId(),
                ChessGameConvertor.chessGameToJson(chessGame)
        );
    }

    public ChessGame findByGameId(String gameId) {
        String chessGameJson = chessDAO.findChessGameByGameId(gameId);

        return ChessGameConvertor.jsonToChessGame(gameId, chessGameJson);
    }

    public boolean containsByGameId(String gameId) {
        return chessDAO.isGameIdExisting(gameId);
    }
}
