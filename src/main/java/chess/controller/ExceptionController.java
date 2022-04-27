package chess.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(IllegalArgumentException.class)
    public String handle(RedirectAttributes redirectAttributes, IllegalArgumentException e, HttpServletRequest request) {
        redirectAttributes.addAttribute("error", e.getMessage());

        String referer = request.getHeader("Referer");
        String[] split = referer.split("/");
        String[] result = split[4].split("\\?");

        return "redirect:/game/" + result[0];
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public String handleEmptyResult(RedirectAttributes redirectAttributes, EmptyResultDataAccessException e) {
        redirectAttributes.addAttribute("error", "비밀번호가 틀렸습니다.");

        return "redirect:/";
    }
}
