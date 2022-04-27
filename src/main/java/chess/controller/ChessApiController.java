package chess.controller;

import chess.domain.ChessGame;
import chess.domain.Result;
import chess.dto.GameCreationDto;
import chess.service.GameService;
import chess.service.MemberService;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessApiController {

    private final GameService gameService;
    private final MemberService memberService;

    public ChessApiController(final GameService gameService, final MemberService memberService) {
        this.gameService = gameService;
        this.memberService = memberService;
    }

    @GetMapping("/score/{gameId}")
    public ResponseEntity<Result> gameScore(@PathVariable("gameId") Long gameId) {
        final ChessGame chessGame = gameService.findByGameId(gameId);
        final Result result = chessGame.createResult();

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/member")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<Long> addMember(@RequestParam("memberName") final String memberName) {
        final Long memberId = memberService.addMember(memberName);
        return ResponseEntity.created(URI.create("/member/" + memberId)).body(memberId);
    }

    @PostMapping("/move/{gameId}")
    @ResponseBody
    public ResponseEntity<Long> movePiece(@PathVariable("gameId") final Long gameId,
                                          @RequestParam("rawFrom") final String rawFrom,
                                          @RequestParam("rawTo") final String rawTo) {
        gameService.move(gameId, rawFrom, rawTo);

        return ResponseEntity.ok().body(gameId);
    }

    @PostMapping("/terminate/{gameId}")
    @ResponseBody
    public ResponseEntity<Long> terminateGame(@PathVariable("gameId") final Long gameId) {
        gameService.terminate(gameId);

        return ResponseEntity.ok().body(gameId);
    }

    @PostMapping("/chessGame")
    @ResponseBody
    public ResponseEntity<Long> createGame(@RequestBody final GameCreationDto gameCreationDto) {
        System.out.println(gameCreationDto);
        final Long gameId = gameService.createGame(
                gameCreationDto.getTitle(),
                gameCreationDto.getPassword(),
                gameCreationDto.getWhiteId(),
                gameCreationDto.getBlackId()
        );

        return ResponseEntity.created(URI.create("/chessGame/" + gameId)).body(gameId);
    }

    @PostMapping("/{gameId}/password")
    @ResponseBody
    public ResponseEntity<Boolean> validatePassword(@PathVariable final Long gameId,
                                                    @RequestParam final String password) {
        gameService.validatePassword(gameId, password);

        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/member/{memberId}")
    @ResponseBody
    public ResponseEntity<Long> deleteMember(@PathVariable("memberId") final Long memberId) {
        memberService.deleteMember(memberId);

        return ResponseEntity.ok().body(memberId);
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<Long> deleteGame(@PathVariable("gameId") final Long gameId,
                                           @RequestParam final String password ) {
        gameService.deleteGameById(gameId, password);

        return ResponseEntity.ok().body(gameId);
    }
}
