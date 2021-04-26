package chess.controller.web;

import chess.controller.web.dto.game.GameIdResponseDto;
import chess.controller.web.dto.game.GameRequestDto;
import chess.controller.web.dto.history.HistoryResponseDto;
import chess.controller.web.dto.move.MoveRequestDto;
import chess.controller.web.dto.move.PathResponseDto;
import chess.controller.web.dto.piece.PieceResponseDto;
import chess.controller.web.dto.score.ScoreResponseDto;
import chess.controller.web.dto.state.StateResponseDto;
import chess.dao.dto.history.HistoryDto;
import chess.dao.dto.piece.PieceDto;
import chess.service.ChessService;
import chess.service.dto.game.GameInfoDto;
import chess.service.dto.move.MoveDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/games")
public class SpringWebChessRestController {

    private final ChessService chessService;
    private final ModelMapper modelMapper;

    public SpringWebChessRestController(ChessService chessService, ModelMapper modelMapper) {
        this.chessService = chessService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("")
    public ResponseEntity<GameIdResponseDto> saveGame(@RequestBody GameRequestDto gameRequestDto) {
        GameInfoDto gameInfoDto = modelMapper.map(gameRequestDto, GameInfoDto.class);
        return ResponseEntity.ok().body(new GameIdResponseDto(chessService.saveGame(gameInfoDto)));
    }

    @GetMapping("/{id}/pieces")
    public ResponseEntity<List<PieceResponseDto>> findPiecesByGameId(@PathVariable Long id) {
        List<PieceDto> pieceDtos = chessService.findPiecesById(id);
        List<PieceResponseDto> pieceResponseDtos = pieceDtos.stream()
                .map(pieceDto -> modelMapper.map(pieceDto, PieceResponseDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(pieceResponseDtos);
    }

    @GetMapping("/{id}/score")
    public ResponseEntity<ScoreResponseDto> findScoreByGameId(@PathVariable Long id) {
        ScoreResponseDto scoreResponseDto = modelMapper.map(chessService.findScoreByGameId(id), ScoreResponseDto.class);
        return ResponseEntity.ok().body(scoreResponseDto);
    }

    @GetMapping("/{id}/state")
    public ResponseEntity<StateResponseDto> findStateByGameId(@PathVariable Long id) {
        StateResponseDto stateResponseDto = modelMapper.map(chessService.findStateByGameId(id), StateResponseDto.class);
        return ResponseEntity.ok().body(stateResponseDto);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<HistoryResponseDto>> findHistoryByGameId(@PathVariable Long id) {
        List<HistoryDto> historyDtos = chessService.findHistoriesByGameId(id);
        List<HistoryResponseDto> historyResponseDtos = historyDtos.stream()
                .map(historyDto -> modelMapper.map(historyDto, HistoryResponseDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(historyResponseDtos);
    }

    @GetMapping("/{id}/path")
    public ResponseEntity<List<String>> movablePath(@PathVariable Long id, @RequestParam String source) {
        System.out.println("asdfgsa controller= " + source + " = ******");
        PathResponseDto pathResponseDto = modelMapper.map(chessService.movablePath(source, id), PathResponseDto.class);
        return ResponseEntity.ok().body(pathResponseDto.getPath());
    }

    @PostMapping("/{id}/move")
    public ResponseEntity<HistoryResponseDto> move(@PathVariable Long id, @RequestBody MoveRequestDto moveRequestDto) {
        MoveDto moveDto = modelMapper.map(moveRequestDto, MoveDto.class);
        HistoryResponseDto historyResponseDto =
                modelMapper.map(chessService.move(moveDto, id), HistoryResponseDto.class);
        return ResponseEntity.ok().body(historyResponseDto);
    }
}
