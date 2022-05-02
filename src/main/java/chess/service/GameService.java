package chess.service;

import chess.dto.ChessGameDto;
import chess.dao.GameDao;
import chess.dao.MemberDao;
import chess.domain.Board;
import chess.domain.BoardInitializer;
import chess.domain.ChessGame;
import chess.domain.Member;
import chess.domain.Participant;
import chess.domain.Result;
import chess.domain.Room;
import chess.domain.piece.detail.Team;
import chess.domain.square.Square;
import chess.dto.GameResultDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final GameDao gameDao;
    private final MemberDao memberDao;

    public GameService(final GameDao gameDao, final MemberDao memberDao) {
        this.gameDao = gameDao;
        this.memberDao = memberDao;
    }

    public Long createGame(final String title, final String password, final Long whiteId, final Long blackId) {
        final Member white = memberDao.findById(whiteId).orElseThrow(() -> new RuntimeException("찾는 멤버가 없음!"));
        final Member black = memberDao.findById(blackId).orElseThrow(() -> new RuntimeException("찾는 멤버가 없음!"));
        final Board board = new Board(BoardInitializer.create());
        final Participant participant = new Participant(white, black);
        final Room room = new Room(title, password, participant);

        return gameDao.save(new ChessGame(board, Team.WHITE, room)).getId();
    }

    public List<ChessGameDto> findPlayingGames() {
        return gameDao.findAll().stream()
                .map(ChessGameDto::from)
                .collect(Collectors.toList());
    }

    public ChessGame findByGameId(final Long gameId) {
        return gameDao.findById(gameId)
                .orElseThrow(() -> new RuntimeException("찾는 게임이 존재하지 않습니다."));
    }

    public List<ChessGame> findHistoriesByMemberId(final Long memberId) {
        return gameDao.findHistoriesByMemberId(memberId);
//        return games.stream()
//                .map(game -> toGameResultDTO(game, memberId))
//                .collect(Collectors.toList());
    }

    public void move(final Long gameId, final String rawFrom, final String rawTo) {
        final ChessGame chessGame = findByGameId(gameId);
        chessGame.move(Square.from(rawFrom), Square.from(rawTo));
        updateGameByMove(chessGame, rawFrom, rawTo);
    }

    public void terminate(final Long gameId) {
        final ChessGame game = findByGameId(gameId);
        game.terminate();
        gameDao.terminate(game);
    }

    public void validatePassword(final Long gameId, final String password) {
        final ChessGame game = gameDao.findById(gameId)
                .orElseThrow(() -> new RuntimeException("찾는 게임이 존재하지 않습니다."));

        if (!game.getRoom().getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    private void updateGameByMove(final ChessGame chessGame, final String rawFrom, final String rawTo) {
        gameDao.updateByMove(chessGame, rawFrom, rawTo);
    }

    public void deleteGameById(final Long gameId, final String password) {
        final ChessGame game = gameDao.findById(gameId)
                .orElseThrow(() -> new RuntimeException("찾는 게임이 존재하지 않습니다."));
        validatePassword(gameId, password);
        validateInProgress(game);

        gameDao.deleteGameById(gameId);
    }

    private void validateInProgress(final ChessGame game) {
        if (game.isInProgress()) {
            throw new IllegalStateException("진행 중인 게임은 삭제할 수 없습니다.");
        }
    }
}
