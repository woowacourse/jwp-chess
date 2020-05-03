package wooteco.chess.service;

import static wooteco.chess.view.response.ResponseStatus.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.game.Game;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.result.Result;
import wooteco.chess.repository.jdbc.GameEntity;
import wooteco.chess.repository.jdbc.GameRepository;
import wooteco.chess.view.dto.requestdto.PositionRequestDto;
import wooteco.chess.view.dto.responsedto.BoardDto;
import wooteco.chess.view.dto.responsedto.GameDto;
import wooteco.chess.view.dto.responsedto.ScoreDto;
import wooteco.chess.view.response.ResponseDto;

@Service
public class GameService {
    private static final String NONE_ELEMENT_QUERY_RESULT_EXCEPTION_MESSAGE = "조건에 해당하는 요소가 없습니다.";
    private static final Long DEFAULT_USER_ID = 1L;

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public ResponseDto calculateScore() {
        return makeResponse(() -> {
            Game game = findGame();
            Result status = game.status();
            List<ScoreDto> scores = status.getStatus().entrySet().stream()
                    .map(ScoreDto::new)
                    .collect(Collectors.toList());
            return new ResponseDto(SUCCESS, scores);
        });
    }

    public ResponseDto changeState(String request) {
        return makeResponse(() -> {
            GameEntity savedGameEntity = gameRepository.findById(DEFAULT_USER_ID)
                    .orElseThrow(() -> new NoSuchElementException(NONE_ELEMENT_QUERY_RESULT_EXCEPTION_MESSAGE));
            Game game = savedGameEntity.toDomain();
            game = game.changeState(request);
            savedGameEntity.update(game);
            gameRepository.save(savedGameEntity);
            return new ResponseDto(SUCCESS);
        });
    }

    public ResponseDto findAllPiecesOnBoard() {
        return makeResponse(() -> {
            Game game = findGame();
            Board board = game.getBoard();

            List<BoardDto> pieces = board.getPieces().entrySet().stream()
                    .map(entry -> new BoardDto(entry.getKey().toString(), entry.getValue().getSymbol()))
                    .collect(Collectors.toList());
            return new ResponseDto(SUCCESS, pieces);
        });
    }

    public ResponseDto move(PositionRequestDto positionRequestDTO) {
        return makeResponse(() -> {
            Position from = Position.of(positionRequestDTO.getFrom());
            Position to = Position.of(positionRequestDTO.getTo());

            GameEntity saveEntity = gameRepository.findById(DEFAULT_USER_ID)
                    .orElseThrow(() -> new NoSuchElementException(NONE_ELEMENT_QUERY_RESULT_EXCEPTION_MESSAGE));
            Game game = saveEntity.toDomain();
            game = game.move(from, to);
            saveEntity.update(game);
            gameRepository.save(saveEntity);

            Board board = game.getBoard();
            List<BoardDto> result = new ArrayList<>();
            result.add(new BoardDto(from.toString(), board.findSymbol(from)));
            result.add(new BoardDto(to.toString(), board.findSymbol(to)));
            return new ResponseDto(SUCCESS, result);
        });
    }

    public ResponseDto getCurrentState() {
        return makeResponse(() -> {
            Game game = findGame();
            return new ResponseDto(SUCCESS, GameDto.of(game.getTurn(), game.getStateType()));
        });
    }

    public ResponseDto getWinner() {
        return makeResponse(() -> {
            Game game = findGame();
            String winner = game.getWinner().name().toLowerCase();
            return new ResponseDto(SUCCESS, winner);
        });
    }

    public ResponseDto isNotFinish() {
        return makeResponse(() -> {
            Game game = findGame();
            return new ResponseDto(SUCCESS, game.isNotFinished());
        });
    }

    private ResponseDto makeResponse(Supplier<ResponseDto> responseGenerator) {
        try {
            return responseGenerator.get();
        } catch (RuntimeException e) {
            return new ResponseDto(ERROR, e.getMessage());
        }
    }

    private Game findGame() {
        GameEntity gameEntity = gameRepository.findById(DEFAULT_USER_ID)
                .orElseThrow(() -> new NoSuchElementException(NONE_ELEMENT_QUERY_RESULT_EXCEPTION_MESSAGE));
        return gameEntity.toDomain();
    }
}