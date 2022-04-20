package chess.controller;

import chess.domain.ChessGame;
import chess.domain.Member;
import chess.domain.Result;
import chess.domain.piece.Piece;
import chess.domain.square.Rank;
import chess.dto.CreateGameRequestDto;
import chess.dto.GameResultDto;
import chess.dto.MoveRequestDto;
import chess.dto.RankDto;
import chess.service.GameService;
import chess.service.MemberService;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SpringWebController {

    private final GameService gameService;
    private final MemberService memberService;

    @Autowired
    public SpringWebController(final GameService gameService, final MemberService memberService) {
        this.gameService = gameService;
        this.memberService = memberService;
    }

    @GetMapping("/")
    public String renderHome(final Model model) {
        final Map<String, Object> attributes = new HashMap<>();
        final List<ChessGame> games = gameService.findPlayingGames();
        final List<Member> members = memberService.findAllMembers();
        attributes.put("games", games);
        attributes.put("members", members);
        model.addAllAttributes(attributes);
        return "index";
    }

    @GetMapping("/play/{gameId}")
    public String renderPlayGame(final Model model, @PathVariable("gameId") final Long gameId) {
        final ChessGame chessGame = gameService.findByGameId(gameId);
        if (chessGame.isEnd()) {
            return "redirect:" + String.format("/result/%d", gameId);
        }

        final Map<String, Object> attributes = new HashMap<>();
        attributes.put("turn", chessGame.getTurn());
        attributes.put("ranks", makeRanksDto(chessGame));
        attributes.put("gameId", gameId);
        model.addAllAttributes(attributes);
        return "play";
    }

    private List<RankDto> makeRanksDto(final ChessGame chessGame) {
        final List<RankDto> ranks = new ArrayList<>();
        for (int i = 8; i > 0; i--) {
            final List<Piece> pieces = chessGame.getBoard()
                    .getPiecesByRank(Rank.from(i));
            ranks.add(RankDto.toDto(pieces, i));
        }
        return ranks;
    }

    @GetMapping("/result/{gameId}")
    public String renderGameResult(final Model model, @PathVariable("gameId") final Long gameId) {
        final ChessGame chessGame = gameService.findByGameId(gameId);
        final Result result = chessGame.createResult();

        final Map<String, Object> attributes = new HashMap<>();
        attributes.put("winner", result.getWinner().name());
        attributes.put("whiteScore", result.getWhiteScore());
        attributes.put("blackScore", result.getBlackScore());
        model.addAllAttributes(attributes);
        return "result";
    }

    @GetMapping("/history/{memberId}")
    public String renderMemberHistory(final Model model, @PathVariable("memberId") final Long memberId) {
        final List<GameResultDto> history = gameService.findHistoriesByMemberId(memberId);
        final Map<String, Object> attributes = new HashMap<>();
        attributes.put("history", history);
        model.addAllAttributes(attributes);
        return "history";
    }

    @GetMapping("/member-management")
    public String renderMemberManagement(final Model model) {
        final List<Member> members = memberService.findAllMembers();
        final Map<String, Object> attributes = new HashMap<>();
        attributes.put("members", members);
        model.addAllAttributes(attributes);
        return "member-management";
    }

    @GetMapping("/score/{gameId}")
    @ResponseBody
    public Result getGameScore(@PathVariable final Long gameId) {
        final ChessGame chessGame = gameService.findByGameId(gameId);
        return chessGame.createResult();
    }

    @PostMapping("/member")
    @ResponseBody
    public ResponseEntity<Long> addMember(@RequestBody final Member member) {
        final Long memberId = memberService.addMember(member.getName());
        return ResponseEntity.created(URI.create("/member/" + memberId)).body(memberId);
    }

    @GetMapping("/member/{memberId}")
    @ResponseBody
    public Member getMember(@PathVariable("memberId") final Long memberId) {
        return memberService.findById(memberId);
    }

    @PutMapping("/move/{gameId}")
    public ResponseEntity<Long> movePiece(@PathVariable final Long gameId,
                                            @RequestBody final MoveRequestDto moveRequestDto) {
        gameService.move(gameId, moveRequestDto.getSource(), moveRequestDto.getTarget());
        return ResponseEntity.ok().body(gameId);
    }

    @PutMapping("/terminate/{gameId}")
    public ResponseEntity<Long> terminateGame(@PathVariable final Long gameId) {
        gameService.terminate(gameId);
        return ResponseEntity.ok().body(gameId);
    }

    @PostMapping("/chessGame")
    public ResponseEntity<Long> createGame(@RequestBody final CreateGameRequestDto createGameRequestDto) {
        final Long gameId = gameService.createGame(createGameRequestDto.getWhiteId(), createGameRequestDto.getBlackId());
        return ResponseEntity.created(URI.create("/chessGame/" + gameId)).body(gameId);
    }

    @DeleteMapping("/member/{memberId}")
    public ResponseEntity<Long> deleteMember(@PathVariable final Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.ok().body(memberId);
    }
}
