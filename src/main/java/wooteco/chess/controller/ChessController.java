package wooteco.chess.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wooteco.chess.controller.dto.ChessPieceDto;
import wooteco.chess.controller.dto.ChessWebIndexDto;
import wooteco.chess.controller.dto.PieceDto;
import wooteco.chess.controller.dto.ResponseDto;
import wooteco.chess.domain.player.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.service.ChessService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ChessController {

    private ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index(Model model) {
        ChessWebIndexDto chessWebIndexDto = ChessWebIndexDto.of(chessService.getRoomId());
        model.addAttribute("chessRoomId", chessWebIndexDto);
        return "index";
    }

    @GetMapping("/chess/{id}")
    public String chess(@PathVariable Long id, Model model) {
        chessService.load(id);
        List<ChessPieceDto> pieces = getPieceDto(id);
        model.addAttribute("chessPiece", pieces);
        model.addAttribute("id", id);
        return "chess";
    }

    @PostMapping("createChessGame")
    @ResponseBody
    public Long createChessGame() {
        return chessService.createGame();
    }

    @PostMapping("/restart/{id}")
    @ResponseBody
    public ResponseEntity restartGame(@PathVariable Long id) {
        chessService.restart(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/save/{id}")
    @ResponseBody
    public ResponseEntity save(@PathVariable Long id) {
        chessService.save(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/end/{id}")
    @ResponseBody
    public ResponseEntity end(@PathVariable Long id) {
        chessService.remove(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/move/{id}")
    @ResponseBody
    public boolean move(@PathVariable Long id, @RequestParam String source, @RequestParam String target) {
        List<String> parameters = Arrays.asList(source, target);
        chessService.move(id, parameters);
        return chessService.isEnd(id);
    }

    @GetMapping("/status/{id}")
    @ResponseBody
    public String status(@PathVariable Long id) {
        Team team = chessService.getCurrentTeam(id);
        return team + "팀 " + chessService.getScore(id) + "점";
    }

    @GetMapping("/movable/{id}/{position}")
    @ResponseBody
    public String movable(@PathVariable Long id, @PathVariable String position) {
        Position source = Position.of(position);
        List<Position> positions = chessService.getMovablePositions(id, source);
        return positions.stream().map(Position::getName).collect(Collectors.joining(","));
    }

    private List<ChessPieceDto> getPieceDto(Long id) {
        ResponseDto responseDto = chessService.getResponseDto(id);
        Map<Position, PieceDto> board = responseDto.getBoard();
        return board.entrySet().stream()
                .map(entry ->
                        new ChessPieceDto(
                                entry.getKey().getName(),
                                entry.getValue().getPieceType(),
                                entry.getValue().getTeam())
                )
                .collect(Collectors.toList());
    }

}
