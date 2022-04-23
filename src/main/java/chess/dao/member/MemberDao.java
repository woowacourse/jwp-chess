package chess.dao.member;

import chess.domain.Member;
import java.util.List;
import java.util.Optional;

public interface MemberDao {

    Long save(final Member member);

    Optional<Member> findById(final Long id);

    List<Member> findAll();

    void deleteById(final Long id);
}
