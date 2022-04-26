package chess.service;

import chess.dao.MemberDao;
import chess.domain.Member;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<Member> findAllMembers() {
        return memberDao.findAll();
    }

    public Long addMember(final String memberName) {
        return memberDao.save(new Member(memberName));
    }

    public void deleteMember(final Long memberId) {
        memberDao.deleteById(memberId);
    }
}

