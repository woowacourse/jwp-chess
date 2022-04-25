package chess.model.service.fake;

import chess.model.member.Member;
import chess.repository.MemberRepository;

import java.util.List;

public class FakeMemberRepository implements MemberRepository<Member> {

    private int fakeAutoIncrementId;
    private String fakeName;

    public FakeMemberRepository(int fakeAutoIncrementId, String fakeName) {
        this.fakeAutoIncrementId = fakeAutoIncrementId;
        this.fakeName = fakeName;
    }

    @Override
    public List<Member> findMembersByRoomId(int roomId) {
        return List.of(
                new Member(fakeAutoIncrementId, fakeName, roomId),
                new Member(fakeAutoIncrementId + 1, fakeName + 1, roomId));
    }

    @Override
    public Member save(String name, int roomId) {
        return null;
    }

    @Override
    public void saveAll(List<Member> members, int roomId) {

    }
}
