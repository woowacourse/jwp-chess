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

    private final RoomService roomService;
    private final GameService gameService;
    private final MovePathService movePathService;
    private final ModelMapper modelMapper;
    private final MoveService moveService;
    private final HistoryService historyService;

    public GameRestController(RoomService roomService, GameService gameService, MovePathService movePathService, ModelMapper modelMapper, MoveService moveService, HistoryService historyService) {
        this.roomService = roomService;
        this.gameService = gameService;
        this.movePathService = movePathService;
        this.modelMapper = modelMapper;
        this.moveService = moveService;
        this.historyService = historyService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDto> findGameByGameId(@PathVariable Long id) {
        GameResponseDto gameResponseDto = new ModelMapper().map(gameService.findById(id), GameResponseDto.class);
        return ResponseEntity.ok().body(gameResponseDto);
    }

    @GetMapping("/{id}/pieces")
    public ResponseEntity<List<PieceResponseDto>> findPiecesByGameId(@PathVariable Long id) {
        List<PieceDto> pieceDtos = gameService.findPiecesById(id);
        List<PieceResponseDto> pieceResponseDtos = pieceDtos.stream()
                .map(pieceDto -> modelMapper.map(pieceDto, PieceResponseDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(pieceResponseDtos);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<HistoryResponseDto>> findHistoryByGameId(@PathVariable Long id) {
        List<HistoryDto> historyDtos = historyService.findHistoriesByGameId(id);
        List<HistoryResponseDto> historyResponseDtos = historyDtos.stream()
                .map(historyDto -> modelMapper.map(historyDto, HistoryResponseDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(historyResponseDtos);
    }

    @GetMapping("/{gameId}/path")
    public ResponseEntity<List<String>> movablePath(@PathVariable Long gameId, @RequestParam String source, HttpSession httpSession) {
        String password = (String) httpSession.getAttribute("password");
        PathResponseDto pathResponseDto = modelMapper.map(movePathService.movablePath(source, gameId, password), PathResponseDto.class);
        return ResponseEntity.ok().body(pathResponseDto.getPath());
    }

    @PostMapping("/{id}/move")
    public ResponseEntity<HistoryResponseDto> move(@PathVariable Long id, @RequestBody MoveRequestDto moveRequestDto) {
        MoveDto moveDto = modelMapper.map(moveRequestDto, MoveDto.class);
        HistoryResponseDto historyResponseDto =
                modelMapper.map(moveService.move(moveDto, id), HistoryResponseDto.class);
        return ResponseEntity.ok().body(historyResponseDto);
    }
}
