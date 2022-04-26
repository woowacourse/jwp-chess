package chess.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import chess.domain.ChessGame;
import chess.domain.Member;
import chess.domain.Result;
import chess.domain.piece.Piece;
import chess.domain.square.Rank;
import chess.dto.GameResultDto;
import chess.dto.RankDto;
import chess.dto.ResponseDto;
import chess.service.GameService;
import chess.service.MemberService;

@Controller
public class ChessController {

    private final GameService gameService;
    private final MemberService memberService;

    public ChessController(final GameService gameService, final MemberService memberService) {
        this.gameService = gameService;
        this.memberService = memberService;
    }

    @GetMapping("/")
    public String index(final Model model) {
        final List<ChessGame> games = gameService.findPlayingGames();
        final List<Member> members = memberService.findAllMembers();
        model.addAttribute("games", games);
        model.addAttribute("members", members);
        return "index";
    }

    @GetMapping("/play/{gameId}")
    public String playGame(@PathVariable("gameId") Long gameId, final Model model) {
        final ChessGame chessGame = gameService.findByGameId(gameId);
        if (chessGame.isEnd()) {
            return "redirect:/result/" + gameId;
        }

        model.addAttribute("turn", chessGame.getTurn());
        model.addAttribute("ranks", makeRanksDto(chessGame));
        model.addAttribute("gameId", gameId);
        return "play";
    }

    @GetMapping("/result/{gameId}")
    public String gameResult(@PathVariable("gameId") Long gameId, final Model model) {
        final ChessGame chessGame = gameService.findByGameId(gameId);
        final Result result = chessGame.createResult();

        model.addAttribute("winner", result.getWinner().name());
        model.addAttribute("whiteScore", result.getWhiteScore());
        model.addAttribute("blackScore", result.getBlackScore());
        return "result";
    }

    @GetMapping("/history/{memberId}")
    public String history(@PathVariable("memberId") Long memberId, final Model model) {
        final List<GameResultDto> history = gameService.findHistoriesByMemberId(memberId);
        model.addAttribute("history", history);
        return "history";
    }

    @GetMapping("/member-management")
    public String memberManagement(final Model model) {
        final List<Member> members = memberService.findAllMembers();
        model.addAttribute("members", members);
        return "member-management";
    }

    private List<RankDto> makeRanksDto(final ChessGame chessGame) {
        final List<RankDto> ranks = new ArrayList<>();
        for (int i = 8; i > 0; i--) {
            final List<Piece> pieces = chessGame.getBoard().getPiecesByRank(Rank.from(i));
            ranks.add(RankDto.toDto(pieces, i));
        }
        return ranks;
    }
}
