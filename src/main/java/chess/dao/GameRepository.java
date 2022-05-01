package chess.dao;

import chess.entities.MemberEntity;
import chess.entities.GameEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepository {

    private final BoardDao<GameEntity> boardDao;
    private final MemberDao<MemberEntity> memberDao;

    public GameRepository(BoardDao<GameEntity> boardDao, MemberDao<MemberEntity> memberDao) {
        this.boardDao = boardDao;
        this.memberDao = memberDao;
    }

    public GameEntity getById(int id) {
        List<MemberEntity> memberEntities = memberDao.getAllByBoardId(id);
        GameEntity gameEntity = boardDao.getById(id);
        return new GameEntity(gameEntity.getId(), gameEntity.getRoomTitle(), gameEntity.getTurn(), memberEntities,
                gameEntity.getPassword());
    }

    public List<GameEntity> findAll() {
        List<GameEntity> gameEntities = boardDao.findAll();
        List<GameEntity> gamesWithMemberEntities = new ArrayList<>();
        for (GameEntity gameEntity : gameEntities) {
            gamesWithMemberEntities.add(new GameEntity(gameEntity.getId(), gameEntity.getRoomTitle(), gameEntity.getTurn(),
                    memberDao.getAllByBoardId(gameEntity.getId()), gameEntity.getPassword()));
        }
        return gamesWithMemberEntities;
    }
}
