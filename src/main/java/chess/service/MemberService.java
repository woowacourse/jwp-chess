package chess.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import chess.dao.member.MemberDao;
import chess.domain.Member;

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

    public Member findById(final Long id) {
        return memberDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException("멤버를 찾을 수 없습니다."));
    }
}

