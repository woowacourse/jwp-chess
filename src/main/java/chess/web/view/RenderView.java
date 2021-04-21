package chess.web.view;

import chess.domain.game.ChessGame;
import chess.web.dto.BoardDto;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.TemplateEngine;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class RenderView {

    private static final TemplateEngine TEMPLATE_ENGINE = new HandlebarsTemplateEngine();

    private RenderView() {
    }

    public static Map<String, Object> renderBoard(ChessGame chessGame) {
        Map<String, Object> boardDto = new BoardDto(chessGame).getResult();
        Map<String, Object> model = new HashMap<>();

        String boardHtml = renderHtml(boardDto, "/board.hbs");
        model.put("board", boardHtml);
        model.put("currentTurn", chessGame.currentTurn());
        return model;
    }

    public static String renderHtml(Map<String, Object> model, String templatePath) {
        return TEMPLATE_ENGINE.render(new ModelAndView(model, templatePath));
    }
}