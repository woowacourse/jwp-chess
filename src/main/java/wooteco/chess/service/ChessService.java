package wooteco.chess.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.chess.controller.dto.ChessRoomResponseDto;
import wooteco.chess.controller.dto.MoveRequestDto;
import wooteco.chess.controller.dto.ResponseDto;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.initializer.AutomatedBoardInitializer;
import wooteco.chess.domain.game.ChessGame;
import wooteco.chess.domain.player.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.repository.ChessGameTable;
import wooteco.chess.repository.ChessGameTableRepository;

@Service
public class ChessService {

    private ChessGameTableRepository chessGameRepository;
    private Map<Long, ChessGame> chessGames = new HashMap<>();

    public ChessService(ChessGameTableRepository chessGameRepository) {
        this.chessGameRepository = chessGameRepository;
    }

    public Long createGame() {
        ChessGame chessGame = ChessGame.of(Board.of(new AutomatedBoardInitializer()), Team.WHITE);
        return chessGameRepository.save(ChessGameTable.createForSave(chessGame)).getId();
    }

    public void restart(final Long id) {
        loadIfNotExisting(id);
        ChessGame chessGame = ChessGame.of(id, Board.of(new AutomatedBoardInitializer()), Team.WHITE);
        chessGames.put(id, chessGame);
        chessGameRepository.save(ChessGameTable.createForUpdate(chessGame));
    }

    public void load(final Long id) {
        ChessGame chessGame = findChessGame(id);
        chessGames.put(id, chessGame);
    }

    public void save(final Long id) {
        loadIfNotExisting(id);
        ChessGame chessGame = chessGames.get(id);
        chessGameRepository.save(ChessGameTable.createForUpdate(chessGame));
    }

    public void remove(final Long id) {
        loadIfNotExisting(id);
        chessGameRepository.deleteById(id);
        chessGames.remove(id);
    }

    public boolean isEnd(final Long id) {
        loadIfNotExisting(id);
        ChessGame chessGame = chessGames.get(id);
        return chessGame.isEnd();
    }

    public void move(final Long id, final MoveRequestDto moveRequestDto) {
        loadIfNotExisting(id);
        ChessGame chessGame = chessGames.get(id);
        chessGame.move(moveRequestDto.generateMoveParameter());
    }

    public List<Position> getMovablePositions(final Long id, final Position source) {
        loadIfNotExisting(id);
        ChessGame chessGame = chessGames.get(id);
        return chessGame.getMovablePositions(source);
    }

    public ResponseDto getResponseDto(final Long id) {
        loadIfNotExisting(id);
        return ResponseDto.of(chessGames.get(id));
    }

    public double getScore(Long id) {
        loadIfNotExisting(id);
        ChessGame chessGame = chessGames.get(id);
        return chessGame.getScore();
    }

    public Team getCurrentTeam(Long id) {
        loadIfNotExisting(id);
        ChessGame chessGame = chessGames.get(id);
        return chessGame.getTurn();
    }

    public List<ChessRoomResponseDto> getRoomIds() {
        return chessGameRepository.findRoomIds().stream().map(ChessRoomResponseDto::new).collect(Collectors.toList());
    }

    private ChessGame findChessGame(final Long id) {
        Optional<ChessGameTable> chessGameTableOptional = chessGameRepository.findById(id);
        ChessGameTable chessGameTable = chessGameTableOptional.orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 게임 번호입니다."));
        return chessGameTable.toChessGame();
    }

    private void loadIfNotExisting(final Long id) {
        if (!chessGames.containsKey(id)) {
            load(id);
        }
    }
}
