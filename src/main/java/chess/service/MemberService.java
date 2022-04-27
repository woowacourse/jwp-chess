package chess.service;

import chess.dao.member.MemberDao;
import chess.domain.Member;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberDao memberDao;

    @Autowired
    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<Member> findAllMembers() {
        return memberDao.findAll();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Long addMember(final String memberName) {
        return memberDao.save(new Member(memberName));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteMember(final Long memberId) {
        memberDao.deleteById(memberId);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Member findById(final Long id) {
        return memberDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException("멤버를 찾을 수 없습니다."));
    }
}

