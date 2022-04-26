package chess.service;

import chess.dao.game.GameDao;
import chess.dao.member.MemberDao;
import chess.domain.Board;
import chess.domain.BoardInitializer;
import chess.domain.ChessGame;
import chess.domain.Member;
import chess.domain.Participant;
import chess.domain.Result;
import chess.domain.RoomInfo;
import chess.domain.piece.detail.Team;
import chess.domain.square.Square;
import chess.dto.GameResultDto;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {

    private final GameDao gameDao;
    private final MemberDao memberDao;

    @Autowired
    public GameService(final GameDao gameDao, final MemberDao memberDao) {
        this.gameDao = gameDao;
        this.memberDao = memberDao;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Long createGame(final String title, final String password, final Long whiteId, final Long blackId) {
        final Member white = memberDao.findById(whiteId).orElseThrow(() -> new NoSuchElementException("찾는 멤버가 없음!"));
        final Member black = memberDao.findById(blackId).orElseThrow(() -> new NoSuchElementException("찾는 멤버가 없음!"));
        final Board board = new Board(BoardInitializer.create());
        final Participant participant = new Participant(white, black);

        return gameDao.save(new ChessGame(board, Team.WHITE, new RoomInfo(title, password, participant)));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<ChessGame> findPlayingGames() {
        return gameDao.findAll()
                .stream()
                .filter(game -> !game.isEnd())
                .collect(Collectors.toList());
    }


    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ChessGame findByGameId(final Long gameId) {
        return gameDao.findById(gameId)
                .orElseThrow(() -> new NoSuchElementException("찾는 게임이 존재하지 않습니다."));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<GameResultDto> findHistoriesByMemberId(final Long memberId) {
        final List<ChessGame> games = gameDao.findHistoriesByMemberId(memberId);
        return games.stream()
                .map(game -> toGameResultDTO(game, memberId))
                .collect(Collectors.toList());
    }

    private GameResultDto toGameResultDTO(final ChessGame game, final Long memberId) {
        final String winResult = findWinResult(game, memberId);
        final String enemyName = findEnemyName(game, memberId);
        final String team = findTeam(game, memberId);
        final double myScore = findMyScore(game, memberId);
        final double enemyScore = findEnemyScore(game, memberId);
        return new GameResultDto(winResult, enemyName, team, myScore, enemyScore);
    }

    private static String findWinResult(final ChessGame game, final Long memberId) {
        final Long winnerId = game.getWinnerId();
        if (winnerId.equals(memberId)) {
            return "승";
        }
        return "패";
    }

    private static String findEnemyName(final ChessGame game, final Long memberId) {
        if (game.getParticipant().getBlackId().equals(memberId)) {
            return game.getParticipant().getWhiteName();
        }
        return game.getParticipant().getBlackName();
    }

    private static String findTeam(final ChessGame game, final Long memberId) {
        if (game.getParticipant().getBlackId().equals(memberId)) {
            return "흑";
        }
        return "백";
    }

    private static double findMyScore(final ChessGame game, final Long memberId) {
        final Result result = game.createResult();

        if (game.getParticipant().getBlackId().equals(memberId)) {
            return result.getBlackScore();
        }
        return result.getWhiteScore();
    }

    private static double findEnemyScore(final ChessGame game, final Long memberId) {
        final Result result = game.createResult();

        if (game.getParticipant().getBlackId().equals(memberId)) {
            return result.getWhiteScore();
        }
        return result.getBlackScore();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public synchronized void move(final Long gameId, final String rawFrom, final String rawTo) {
        final ChessGame chessGame = findByGameId(gameId);
        chessGame.move(Square.from(rawFrom), Square.from(rawTo));
        gameDao.move(chessGame, rawFrom, rawTo);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void terminate(final Long gameId) {
        final ChessGame chessGame = findByGameId(gameId);
        chessGame.terminate();
        gameDao.terminate(gameId);
    }

    public Long deleteGame(final Long gameId) {
        return gameDao.deleteById(gameId);
    }
}

