package chess.controller;

import java.util.Optional;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public ModelAndView handle(HttpServletRequest request) {
        Optional<Object> maybeStatus =
                Optional.ofNullable(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
        return maybeStatus.map(this::withError).orElseGet(this::withoutError);
    }

    private ModelAndView withoutError() {
        ModelAndView directAccessModelAndView = new ModelAndView("error");
        directAccessModelAndView.addObject("msg", "이 페이지에 대한 접근은 허용되지 않습니다.");
        return directAccessModelAndView;
    }

    private ModelAndView withError(Object status) {
        int statusCode = Integer.parseInt(status.toString());
        ModelAndView modelAndView = new ModelAndView("error");

        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            modelAndView.addObject("msg", "잘못된 접근입니다.");
            return modelAndView;
        }

        modelAndView.addObject("msg", "서버가 처리할 수 없는 요청입니다.");
        return modelAndView;
    }
}
