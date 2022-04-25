package chess.repository;

import java.util.List;

public interface MemberRepository<T> {

    List<T> findMembersByRoomId(int roomId);

    T save(String name, int roomId);

    void saveAll(List<T> members, int roomId);
}
