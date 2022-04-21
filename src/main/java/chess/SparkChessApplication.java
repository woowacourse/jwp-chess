package chess;

import chess.controller.SparkWebController;
import chess.dao.SqlExecutor;
import chess.dao.game.JdbcGameDao;
import chess.dao.game.JdbcPieceDao;
import chess.dao.member.JdbcMemberDao;
import chess.dao.member.MemberDao;
import chess.service.GameService;
import chess.service.MemberService;

public class SparkChessApplication {
    public static void main(String[] args) {
        final SqlExecutor sqlExecutor = SqlExecutor.getInstance();
        final MemberDao memberDao = new JdbcMemberDao(sqlExecutor);
        final JdbcPieceDao pieceDao = new JdbcPieceDao(sqlExecutor);
        final SparkWebController controller = new SparkWebController(
                new GameService(
                        new JdbcGameDao(pieceDao, memberDao, sqlExecutor),
                        new JdbcMemberDao(sqlExecutor)
                ),
                new MemberService(memberDao)
        );
        controller.run();
    }
}
