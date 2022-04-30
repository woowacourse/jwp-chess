package chess.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ExceptionsController {

    @ExceptionHandler(IllegalArgumentException.class)
    public String handle(RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest,
                         IllegalArgumentException e) {
        String roomId = httpServletRequest.getRequestURI().replaceAll("[^0-9]", ""); // /room/id/command
        redirectAttributes.addAttribute("message", e.getMessage());
        return "redirect:/room/" + roomId;
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handle(RuntimeException e) {
        e.printStackTrace();
        ModelAndView modelAndView = new ModelAndView("exception");
        modelAndView.addObject("message", "서버에서 예외 오류가 발생했습니다.");
        return modelAndView;
    }
}
