package chess;

import chess.controller.WebController;
import chess.dao.DatabaseGameDao;
import chess.dao.DatabaseMemberDao;
import chess.service.GameService;
import chess.service.MemberService;

public class SparkApplication {

    public static void main(String[] args) {
        final WebController controller = new WebController(
                new GameService(new DatabaseGameDao(new DatabaseMemberDao()), new DatabaseMemberDao()),
                new MemberService(new DatabaseMemberDao())
        );
        controller.run();
    }
}
