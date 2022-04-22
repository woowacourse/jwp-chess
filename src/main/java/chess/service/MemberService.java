package chess.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chess.dao.MemberDao;
import chess.domain.Member;

@Service
public class MemberService {

    @Autowired
    private MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<Member> findAllMembers() {
        return memberDao.findAll();
    }

    public void addMember(final String memberName) {
        memberDao.save(new Member(memberName));
    }

    public void deleteMember(final Long memberId) {
        memberDao.deleteById(memberId);
    }
}

