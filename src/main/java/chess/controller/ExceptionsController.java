package chess.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ExceptionsController {

    //TODO: 잘못된 명령어를 입력했을 때 예외처리
    @ExceptionHandler(IllegalArgumentException.class)
    public String handle(RedirectAttributes redirectAttributes, IllegalArgumentException e) {
        redirectAttributes.addAttribute("message", e.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handle(RuntimeException e) {
        e.printStackTrace();
        ModelAndView modelAndView = new ModelAndView("exception");
        modelAndView.addObject("message", "서버에서 예외 오류가 발생했습니다.");
        return modelAndView;
    }
}
