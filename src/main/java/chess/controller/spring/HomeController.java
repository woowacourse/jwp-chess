package chess.controller.spring;

import chess.domain.ChessGame;
import chess.domain.Member;
import chess.service.GameService;
import chess.service.MemberService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private final GameService gameService;
    private final MemberService memberService;

    public HomeController(final GameService gameService, final MemberService memberService) {
        this.gameService = gameService;
        this.memberService = memberService;
    }

    @GetMapping("/")
    public ModelAndView renderHome() {
        final Map<String, Object> attributes = new HashMap<>();
        final List<ChessGame> games = gameService.findPlayingGames();
        final List<Member> members = memberService.findAllMembers();
        attributes.put("games", games);
        attributes.put("members", members);
        return new ModelAndView("index", attributes);
    }
}
