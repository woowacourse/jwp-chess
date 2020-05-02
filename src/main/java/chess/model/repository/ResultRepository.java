package chess.model.repository;

import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ResultRepository extends CrudRepository<ResultEntity, Integer> {

    @Query("SELECT * FROM RESULT WHERE USER_NM = :USER_NM")
    Optional<ResultEntity> findByUserName(@Param("USER_NM") String name);
}