package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.domain.entity.ChessGameEntity;
import wooteco.chess.domain.game.Board;
import wooteco.chess.domain.game.ChessGame;
import wooteco.chess.domain.game.Turn;
import wooteco.chess.domain.game.exception.InvalidTurnException;
import wooteco.chess.domain.game.exception.RoomNotFoundException;
import wooteco.chess.domain.game.state.Playing;
import wooteco.chess.domain.game.state.Ready;
import wooteco.chess.domain.piece.Position;
import wooteco.chess.domain.piece.exception.NotMovableException;
import wooteco.chess.domain.repository.ChessRepository;
import wooteco.chess.dto.*;

@Service
public class ChessService {
    private ChessRepository chessRepository;

    public ChessService(ChessRepository chessRepository) {
        this.chessRepository = chessRepository;
    }

    public ResponseDto createChessRoom() {
        ChessGame chessGame = new ChessGame(new Ready());
        chessGame.start();
        ChessGameEntity chessGameEntity = new ChessGameEntity(chessGame);
        chessGameEntity = chessRepository.save(chessGameEntity);
        return new ResponseDto(ResponseDto.SUCCESS, chessGameEntity.getId());
    }

    public ResponseDto restartGame(int chessGameId) {
        ChessGameEntity chessGameEntity = chessRepository.findById(chessGameId)
                .orElseThrow(RoomNotFoundException::new);
        ChessGame chessGame = new ChessGame(new Playing(Board.create(), Turn.WHITE));
        chessGameEntity.update(chessGame);
        chessRepository.save(chessGameEntity);
        return new ResponseDto(ResponseDto.SUCCESS, chessGameEntity.getId());
    }

    public ResponseDto movePiece(int chessRoomId, Position sourcePosition, Position targetPosition) {
        try {
            return getResponseDtoWithMove(chessRoomId, sourcePosition, targetPosition);
        } catch (NotMovableException | IllegalArgumentException e) {
            return new ResponseDto(ResponseDto.FAIL, "이동할 수 없는 위치입니다.");
        }
    }

    private ResponseDto getResponseDtoWithMove(int chessGameId, Position sourcePosition, Position targetPosition) {
        ChessGameEntity chessGameEntity = chessRepository.findById(chessGameId)
                .orElseThrow(RoomNotFoundException::new);
        ChessGame chessGame = chessGameEntity.createChessGame();
        try {
            chessGame.move(sourcePosition, targetPosition);
            chessGameEntity.update(chessGame);
            chessRepository.save(chessGameEntity);
            return responseChessGame(chessGame);
        } catch (InvalidTurnException e) {
            return new ResponseDto(ResponseDto.FAIL, chessGame.turn().getColor() + "의 턴입니다.");
        }
    }

    public ResponseDto getChessGameById(int chessGameId) {
        ChessGameEntity chessGameEntity = chessRepository.findById(chessGameId)
                .orElseThrow(RoomNotFoundException::new);
        return responseChessGame(chessGameEntity.createChessGame());
    }

    public ResponseDto getGameList() {
        return new ResponseDto(ResponseDto.SUCCESS, chessRepository.selectAll());
    }

    private static ResponseDto responseChessGame(ChessGame chessGame) {
        return new ResponseDto(ResponseDto.SUCCESS,
                new ChessGameDto(new BoardDto(chessGame.board()), new TurnDto(chessGame.turn()),
                        new StatusDto(chessGame.status().getWhiteScore(), chessGame.status().getBlackScore(),
                                chessGame.status().getWinner()), chessGame.isFinished()));
    }
}
