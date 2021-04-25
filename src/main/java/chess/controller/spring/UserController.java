package chess.controller.spring;

import chess.controller.spring.util.CookieParser;
import chess.service.UserService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    public static final String REDIRECT = "redirect:";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    private String renderLogin() {
        return "login-spring";
    }

    @PostMapping("/login")
    private String login(@RequestParam("userName") String userName, HttpServletResponse response) {
        int userId = userService.addUserIfNotExist(userName);
        Cookie cookie = new Cookie("userId", CookieParser.encodeCookie(String.valueOf(userId)));
        response.addCookie(cookie);
        return REDIRECT + "/games";
    }

    @PostMapping("/logout")
    private String logoutAndRedirect(HttpServletResponse response) {
        Cookie cookie = new Cookie("user", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return REDIRECT + "/";
    }
}
