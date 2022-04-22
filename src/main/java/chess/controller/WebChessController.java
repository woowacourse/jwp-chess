package chess.controller;

public class WebChessController {

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }

    private Long parseGameId(String idString) {
        try {
            return Long.parseLong(idString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ILLEGAL_GAME_ID);
        }
    }
}
