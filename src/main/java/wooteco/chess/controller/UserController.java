package wooteco.chess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wooteco.chess.domain.user.User;
import wooteco.chess.service.UserService;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/user")
    public String create(
            @RequestParam("name") String name,
            @RequestParam("password") String password
    ) {
        User user = new User(name, password);
        userService.create(user);
        return "redirect:" + "/room";
    }
}
