package wooteco.chess.repository;

import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import wooteco.chess.domain.player.User;
import wooteco.chess.entity.GameInfoEntity;

public interface GameInfoRepository extends CrudRepository<GameInfoEntity, Long> {

    @Query("select * from "
        + "(select gameinfo.id, black, white, turn, name "
        + "from gameinfo inner join user "
        + "on gameinfo.black=user.id) as tmp  "
        + "where name=\":blackUser\"")
    Optional<GameInfoEntity> findGameInfoByUser(String blackUser, String whiteUser);
}
