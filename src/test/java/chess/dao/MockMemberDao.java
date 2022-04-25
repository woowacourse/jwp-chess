package chess.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import chess.domain.Member;

public class MockMemberDao implements MemberDao {
    private static final Map<Long, Member> store = new ConcurrentHashMap<>();
    private static int nextId = 1;

    @Override
    public Long save(Member member) {
        member = new Member((long) nextId++, member.getName());
        store.put(member.getId(), member);
        return member.getId();
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    void deleteAll() {
        store.clear();
    }
}
