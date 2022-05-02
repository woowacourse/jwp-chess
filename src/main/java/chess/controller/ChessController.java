package chess.controller;

import chess.domain.ChessGame;
import chess.domain.Member;
import chess.domain.Result;
import chess.domain.piece.Piece;
import chess.domain.square.Rank;
import chess.dto.ChessGameDto;
import chess.dto.GameResultDto;
import chess.dto.RankDto;
import chess.service.GameService;
import chess.service.MemberService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChessController {

    private final GameService gameService;
    private final MemberService memberService;

    public ChessController(final GameService gameService, final MemberService memberService) {
        this.gameService = gameService;
        this.memberService = memberService;
    }

    @GetMapping("/")
    public ModelAndView index() {
        final List<ChessGameDto> games = gameService.findPlayingGames();
        final List<Member> members = memberService.findAllMembers();
        final Map<String, Object> model = new HashMap<>();

        model.put("games", games);
        model.put("members", members);

        return new ModelAndView("index", model);
    }

    @GetMapping("/game/{gameId}")
    public ModelAndView playGame(@PathVariable("gameId") Long gameId) {
        final ChessGame chessGame = gameService.findByGameId(gameId);
        final Map<String, Object> model = new HashMap<>();
        if (chessGame.isEnd()) {
            return new ModelAndView("redirect:/game/" + gameId + "/result");
        }

        model.put("turn", chessGame.getTurn());
        model.put("ranks", makeRanksDto(chessGame));
        model.put("gameId", gameId);

        return new ModelAndView("play", model);
    }

    @GetMapping("/game/{gameId}/result")
    public ModelAndView gameResult(@PathVariable("gameId") Long gameId) {
        final ChessGame chessGame = gameService.findByGameId(gameId);
        final Result result = chessGame.createResult();
        final Map<String, Object> model = new HashMap<>();

        model.put("winner", result.getWinner().name());
        model.put("whiteScore", result.getWhiteScore());
        model.put("blackScore", result.getBlackScore());

        return new ModelAndView("result", model);
    }

    @GetMapping("/member/{memberId}/history")
    public ModelAndView history(@PathVariable("memberId") Long memberId) {
        final List<ChessGameDto> history = gameService.findHistoriesByMemberId(memberId).stream()
                .map(ChessGameDto::from)
                .collect(Collectors.toList());

        final Map<String, Object> model = new HashMap<>();
        model.put("history", history);

        return new ModelAndView("history", model);
    }

    @GetMapping("/member-management")
    public ModelAndView memberManagement() {
        final List<Member> members = memberService.findAllMembers();
        final Map<String, Object> model = new HashMap<>();

        model.put("members", members);

        return new ModelAndView("member-management", model);
    }

    private List<RankDto> makeRanksDto(final ChessGame chessGame) {
        final List<RankDto> ranks = new ArrayList<>();
        for (int i = 8; i > 0; i--) {
            final List<Piece> pieces = chessGame.getBoard().getPiecesByRank(Rank.from(i));
            ranks.add(RankDto.of(pieces, i));
        }
        return ranks;
    }
}
