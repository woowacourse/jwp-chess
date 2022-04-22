package chess.repository;

import java.util.List;

public interface MemberRepository<T> {

    List<T> getAllByRoomId(int roomId);

    T save(String name, int roomId);

    void saveAll(List<T> members, int roomId);
}
