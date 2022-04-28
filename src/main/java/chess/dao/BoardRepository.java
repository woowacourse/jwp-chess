package chess.dao;

import chess.entities.Member;
import chess.entities.ChessGame;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class BoardRepository {

    private final BoardDao<ChessGame> boardDao;
    private final MemberDao<Member> memberDao;

    public BoardRepository(BoardDao<ChessGame> boardDao, MemberDao<Member> memberDao) {
        this.boardDao = boardDao;
        this.memberDao = memberDao;
    }

    public ChessGame getById(int id) {
        List<Member> members = memberDao.getAllByBoardId(id);
        ChessGame chessGame = boardDao.getById(id);
        return new ChessGame(chessGame.getId(), chessGame.getRoomTitle(), chessGame.getTurn(), members,
                chessGame.getPassword());
    }

    public List<ChessGame> findAll() {
        List<ChessGame> chessGames = boardDao.findAll();
        List<ChessGame> chessGamesWithMembers = new ArrayList<>();
        for (ChessGame chessGame : chessGames) {
            chessGamesWithMembers.add(new ChessGame(chessGame.getId(), chessGame.getRoomTitle(), chessGame.getTurn(),
                    memberDao.getAllByBoardId(chessGame.getId()), chessGame.getPassword()));
        }
        return chessGamesWithMembers;
    }
}
