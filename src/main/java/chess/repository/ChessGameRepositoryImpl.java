package chess.repository;

import chess.dao.GameDao;
import chess.domain.ChessGame;
import chess.domain.team.BlackTeam;
import chess.domain.team.WhiteTeam;
import dto.MoveDto;
import org.springframework.stereotype.Service;


@Service
public class ChessGameRepositoryImpl implements ChessGameRepository {

    private final GameDao gameDao;
    private final TeamRepositoryImpl teamRepositoryImpl;

    public ChessGameRepositoryImpl(final GameDao gameDao, final TeamRepositoryImpl teamRepositoryImpl) {
        this.gameDao = gameDao;
        this.teamRepositoryImpl = teamRepositoryImpl;
    }


    @Override
    public Long create(final ChessGame chessGame) {
        Long gameId = gameDao.create(chessGame);
        teamRepositoryImpl.create(chessGame.getWhiteTeam(), gameId);
        teamRepositoryImpl.create(chessGame.getBlackTeam(), gameId);
        return gameId;
    }

    @Override
    public ChessGame chessGame(final Long gameId) {
        boolean isEnd = gameDao.isEnd(gameId);

        System.out.println(gameId);
        WhiteTeam whiteTeam = teamRepositoryImpl.whiteTeam(gameId);
        BlackTeam blackTeam = teamRepositoryImpl.blackTeam(gameId);
        whiteTeam.setEnemy(blackTeam);
        blackTeam.setEnemy(whiteTeam);

        return new ChessGame(whiteTeam, blackTeam, isEnd);
    }

    @Override
    public void save(final Long gameId, final ChessGame chessGame, final MoveDto moveDto) {
        boolean isEnd = chessGame.isEnd();

        if (isEnd) {
            gameDao.update(gameId, isEnd);
        }

        teamRepositoryImpl.update(gameId, chessGame.getWhiteTeam());
        teamRepositoryImpl.update(gameId, chessGame.getBlackTeam());
        teamRepositoryImpl.move(gameId, moveDto);
    }
}
