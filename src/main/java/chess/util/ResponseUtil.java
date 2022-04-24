package chess.util;

import org.springframework.web.servlet.ModelAndView;

public class ResponseUtil {

    private static final String RESPONSE_MODEL_KEY = "response";

    private ResponseUtil() {
    }

    public static ModelAndView createModelAndView(String url, Object value) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(url);
        modelAndView.addObject(RESPONSE_MODEL_KEY, value);
        return modelAndView;
    }
}
