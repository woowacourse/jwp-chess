package chess.service;

import java.util.List;

import chess.dao.DatabaseMemberDao;
import chess.dao.MemberDao;
import chess.domain.Member;

public class MemberService {

    private final MemberDao memberDao;

    public MemberService(DatabaseMemberDao databaseMemberDao) {
        this.memberDao = databaseMemberDao;
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

