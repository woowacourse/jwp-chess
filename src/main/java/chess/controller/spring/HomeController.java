package chess.controller.spring;

import chess.domain.ChessGame;
import chess.domain.Member;
import chess.dto.LobbyGameDto;
import chess.service.GameService;
import chess.service.MemberService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        final Map<String, Object> model = new HashMap<>();
        final List<ChessGame> games = gameService.findAllGames();
        final List<Member> members = memberService.findAllMembers();
        final List<LobbyGameDto> gameDtos = gamesToDtos(games);
        model.put("games", gameDtos);
        model.put("members", members);
        return new ModelAndView("index", model);
    }

    private List<LobbyGameDto> gamesToDtos(final List<ChessGame> games) {
        return games.stream()
                .map(game -> new LobbyGameDto(
                        game.getId(),
                        game.getTitle(),
                        game.getParticipant().getWhiteName(),
                        game.getParticipant().getBlackName(),
                        game.isEnd()))
                .collect(Collectors.toList());
    }
}
