package chess.controller.web;

import chess.controller.web.dto.game.GameResponseDto;
import chess.controller.web.dto.history.HistoryResponseDto;
import chess.controller.web.dto.move.MoveRequestDto;
import chess.controller.web.dto.move.PathResponseDto;
import chess.controller.web.dto.piece.PieceResponseDto;
import chess.service.*;
import chess.service.dto.history.HistoryDto;
import chess.service.dto.move.MoveDto;
import chess.service.dto.piece.PieceDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/games")
public class GameRestController {

    private final GameService gameService;
    private final MovePathService movePathService;
    private final MoveService moveService;
    private final HistoryService historyService;
    private final ModelMapper modelMapper;

    public GameRestController(GameService gameService, MovePathService movePathService,
                              MoveService moveService, HistoryService historyService, ModelMapper modelMapper) {
        this.gameService = gameService;
        this.movePathService = movePathService;
        this.moveService = moveService;
        this.historyService = historyService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDto> findGameByGameId(@PathVariable final Long id) {
        GameResponseDto gameResponseDto = new ModelMapper().map(gameService.findById(id), GameResponseDto.class);
        return ResponseEntity.ok().body(gameResponseDto);
    }

    @GetMapping("/{id}/pieces")
    public ResponseEntity<List<PieceResponseDto>> findPiecesByGameId(@PathVariable final Long id) {
        List<PieceDto> pieceDtos = gameService.findPiecesById(id);
        List<PieceResponseDto> pieceResponseDtos = pieceDtos.stream()
                .map(pieceDto -> modelMapper.map(pieceDto, PieceResponseDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(pieceResponseDtos);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<HistoryResponseDto>> findHistoryByGameId(@PathVariable final Long id) {
        List<HistoryDto> historyDtos = historyService.findHistoriesByGameId(id);
        List<HistoryResponseDto> historyResponseDtos = historyDtos.stream()
                .map(historyDto -> modelMapper.map(historyDto, HistoryResponseDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(historyResponseDtos);
    }

    @GetMapping("/{gameId}/path")
    public ResponseEntity<List<String>> movablePath(@PathVariable final Long gameId,
                                                    @RequestParam final String source, final HttpSession httpSession) {
        String password = (String) httpSession.getAttribute("password");
        PathResponseDto pathResponseDto =
                modelMapper.map(movePathService.movablePath(source, gameId, password), PathResponseDto.class);
        return ResponseEntity.ok().body(pathResponseDto.getPath());
    }

    @PostMapping("/{id}/move")
    public ResponseEntity<HistoryResponseDto> move(@PathVariable final Long id,
                                                   @RequestBody final MoveRequestDto moveRequestDto) {
        MoveDto moveDto = modelMapper.map(moveRequestDto, MoveDto.class);
        HistoryResponseDto historyResponseDto =
                modelMapper.map(moveService.move(moveDto, id), HistoryResponseDto.class);
        return ResponseEntity.ok().body(historyResponseDto);
    }
}
