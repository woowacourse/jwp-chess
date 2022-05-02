package chess.controller;

import chess.domain.ChessGame;
import chess.domain.Result;
import chess.dto.GameCreationDto;
import chess.dto.GamePasswordDto;
import chess.dto.MoveCommandDto;
import chess.service.GameService;
import chess.service.MemberService;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/game/{gameId}/score")
    public ResponseEntity<Result> gameScore(@PathVariable("gameId") Long gameId) {
        final ChessGame chessGame = gameService.findByGameId(gameId);
        final Result result = chessGame.createResult();

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/member")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Long> addMember(@RequestParam("memberName") final String memberName) {
        final Long memberId = memberService.addMember(memberName);

        return ResponseEntity.created(URI.create("/member/" + memberId)).body(memberId);
    }

    @PostMapping("/game/{gameId}/terminate")
    public ResponseEntity<Long> terminateGame(@PathVariable("gameId") final Long gameId) {
        gameService.terminate(gameId);

        return ResponseEntity.ok().body(gameId);
    }

    @PostMapping("/chessGame")
    public ResponseEntity<Long> createGame(@RequestBody final GameCreationDto gameCreationDto) {
        final Long gameId = gameService.createGame(
                gameCreationDto.getTitle(),
                gameCreationDto.getPassword(),
                gameCreationDto.getWhiteId(),
                gameCreationDto.getBlackId()
        );

        return ResponseEntity.created(URI.create("/chessGame/" + gameId)).body(gameId);
    }

    @PostMapping("/{gameId}/password")
    public ResponseEntity<Boolean> validatePassword(@PathVariable final Long gameId,
                                                    @RequestBody final GamePasswordDto gamePasswordDto) {
        gameService.validatePassword(gameId, gamePasswordDto.getPassword());

        return ResponseEntity.ok(true);
    }

    @PutMapping("/game/{gameId}/move")
    public ResponseEntity<Long> movePiece(@PathVariable("gameId") final Long gameId,
                                          @RequestBody final MoveCommandDto moveCommandDto) {
        gameService.move(gameId, moveCommandDto.getRawFrom(), moveCommandDto.getRawTo());

        return ResponseEntity.ok().body(gameId);
    }

    @DeleteMapping("/member/{memberId}")
    public ResponseEntity<Long> deleteMember(@PathVariable("memberId") final Long memberId) {
        memberService.deleteMember(memberId);

        return ResponseEntity.ok().body(memberId);
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<Long> deleteGame(@PathVariable("gameId") final Long gameId,
                                           @RequestBody final GamePasswordDto gamePasswordDto) {
        gameService.deleteGameById(gameId, gamePasswordDto.getPassword());

        return ResponseEntity.ok().body(gameId);
    }
}
