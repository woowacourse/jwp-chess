package web.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.exception.InvalidRoomRequestException;
import web.exception.IsRunningChessGameException;

@ControllerAdvice
public class LobbyControllerAdvice {

    @ExceptionHandler(InvalidRoomRequestException.class)
    public String invalidRoomRequestHandler(RedirectAttributes attributes) {
        attributes.addFlashAttribute("hasError", true);
        attributes.addFlashAttribute("errorMessage", "잘못된 방 비밀번호입니다.");
        return "redirect:/";
    }

    @ExceptionHandler(IsRunningChessGameException.class)
    public String isRunningChessGameHandler(RedirectAttributes attributes) {
        attributes.addFlashAttribute("hasError", true);
        attributes.addFlashAttribute("errorMessage", "체스 게임이 진행 중입니다.");
        return "redirect:/";
    }
}
