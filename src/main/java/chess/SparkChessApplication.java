package chess;

import chess.controller.WebController;
import chess.dao.DatabaseGameDao;
import chess.dao.DatabaseMemberDao;
import chess.service.GameService;
import chess.service.MemberService;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;

public class SparkChessApplication {
    public static void main(String[] args) {
        final WebController controller = new WebController(
                new GameService(new DatabaseGameDao(new DatabaseMemberDao()), new DatabaseMemberDao()),
                new MemberService(new DatabaseMemberDao())
        );
        controller.run();
    }
}
