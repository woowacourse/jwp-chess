package wooteco.chess.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import wooteco.chess.domain.game.ChessGame;
import wooteco.chess.domain.game.exception.InvalidTurnException;
import wooteco.chess.domain.game.state.Ready;
import wooteco.chess.domain.piece.Position;
import wooteco.chess.domain.piece.exception.NotMovableException;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.dto.ChessGameDto;
import wooteco.chess.dto.ResponseDto;
import wooteco.chess.dto.StatusDto;
import wooteco.chess.dto.TurnDto;
import wooteco.chess.entity.ChessGameEntity;
import wooteco.chess.repository.ChessGameRepository;

@Service
public class ChessService {
    public static final String GAME_NOT_EXIST_MESSAGE = "해당 게임이 존재하지 않습니다.";
    private final ChessGameRepository chessGameRepository;

    public ChessService(ChessGameRepository chessGameRepository) {
        this.chessGameRepository = chessGameRepository;
    }

    public ResponseDto createChessRoom() {
        ChessGame chessGame = ChessGame.of(new Ready());
        chessGame.start();
        ChessGameEntity chessGameEntity = new ChessGameEntity(chessGame);
        ChessGameEntity savedChessGameEntity = chessGameRepository.save(chessGameEntity);
        return new ResponseDto(ResponseDto.SUCCESS, savedChessGameEntity.getId());
    }

    public ResponseDto restartGame(int chessGameId) {
        ChessGame newChessGame = ChessGame.of(new Ready());
        newChessGame.start();

        ChessGameEntity chessGameEntity = chessGameRepository.findById(chessGameId)
            .orElseThrow(() -> new NoSuchElementException(GAME_NOT_EXIST_MESSAGE));
        chessGameEntity.update(newChessGame);
        chessGameRepository.save(chessGameEntity);
        return new ResponseDto(ResponseDto.SUCCESS, chessGameId);

    }

    public ResponseDto movePiece(int chessGameId, Position sourcePosition, Position targetPosition) {
        ChessGameEntity chessGameEntity = chessGameRepository.findById(chessGameId)
            .orElseThrow(() -> new NoSuchElementException(GAME_NOT_EXIST_MESSAGE));
        ChessGame chessGame = chessGameEntity.toModel();
        try {
            chessGame.move(sourcePosition, targetPosition);
            chessGameEntity.update(chessGame);
            chessGameRepository.save(chessGameEntity);
            return responseChessGame(chessGame);
        } catch (NotMovableException | IllegalArgumentException e) {
            return new ResponseDto(ResponseDto.FAIL, "이동할 수 없는 위치입니다.");
        } catch (InvalidTurnException e) {
            return new ResponseDto(ResponseDto.FAIL, chessGame.turn().getColor() + "의 턴입니다.");
        }
    }

    public ResponseDto getChessGameById(int chessGameId) {
        ChessGameEntity chessGameEntity = chessGameRepository.findById(chessGameId)
            .orElseThrow(() -> new NoSuchElementException(GAME_NOT_EXIST_MESSAGE));
        ChessGame chessGame = chessGameEntity.toModel();
        return responseChessGame(chessGame);
    }

    public ResponseDto getGameListId() {
        return new ResponseDto(ResponseDto.SUCCESS, chessGameRepository.findAllGameId());
    }

    private static ResponseDto responseChessGame(ChessGame chessGame) {
        return new ResponseDto(ResponseDto.SUCCESS,
            new ChessGameDto(new BoardDto(chessGame.board()), new TurnDto(chessGame.turn()),
                new StatusDto(chessGame.status().getWhiteScore(), chessGame.status().getBlackScore(),
                    chessGame.status().getWinner()), chessGame.isFinished()));
    }
}
