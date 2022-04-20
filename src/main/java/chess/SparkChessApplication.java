package chess;

import chess.controller.SparkWebController;
import chess.dao.DatabaseGameDao;
import chess.dao.DatabaseMemberDao;
import chess.service.GameService;
import chess.service.MemberService;

import static spark.Spark.get;

public class SparkChessApplication {
    public static void main(String[] args) {
        final SparkWebController controller = new SparkWebController(
                new GameService(new DatabaseGameDao(new DatabaseMemberDao()), new DatabaseMemberDao()),
                new MemberService(new DatabaseMemberDao())
        );
        controller.run();
    }
}
