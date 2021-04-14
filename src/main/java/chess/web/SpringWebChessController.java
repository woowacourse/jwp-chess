package chess.web;

import chess.service.*;
import chess.web.dto.MessageDto;
import chess.web.dto.MoveDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SpringWebChessController {

    private final StartService startService;
    private final EndService endService;
    private final MoveService moveService;
    private final SaveService saveService;
    private final StatusService statusService;
    private final LoadService loadService;

    public SpringWebChessController(StartService startService,
                                   EndService endService,
                                   MoveService moveService,
                                   SaveService saveService,
                                   StatusService statusService,
                                   LoadService loadService) {
        this.startService = startService;
        this.endService = endService;
        this.moveService = moveService;
        this.saveService = saveService;
        this.statusService = statusService;
        this.loadService = loadService;
    }

    @GetMapping("/{gameId}/start")
    public Object start(@PathVariable String gameId) {
        return startService.startNewGame(gameId);
    }

    @GetMapping("/{gameId}/load")
    public Object load(@PathVariable String gameId) {
        return loadService.loadByGameId(gameId);
    }

    @PatchMapping("/{gameId}/move")
    public Object move(@PathVariable String gameId, @RequestBody MoveDto moveDto) {
        String source = moveDto.getSource();
        String target = moveDto.getTarget();

        return moveService.move(gameId, source, target);
    }

    @PostMapping("/{gameId}/save")
    public Object save(@PathVariable String gameId) {
        return saveService.save(gameId);
    }

    @GetMapping("/{gameId}/status")
    public Object status(@PathVariable String gameId) {
        return statusService.getStatus(gameId);
    }

    @PatchMapping("/{gameId}/status")
    public Object end(@PathVariable String gameId) {
        return endService.end(gameId);
    }

    @ExceptionHandler
    public ResponseEntity<MessageDto> handle(RuntimeException e) {
        return ResponseEntity.badRequest().body(new MessageDto(e.getMessage()));
    }

}
