package chess.service;

import chess.domain.board.Board;
import chess.domain.game.ChessGame;
import chess.domain.game.GameRoom;
import chess.domain.game.GameSwitch;
import chess.domain.game.Turn;
import chess.domain.piece.Team;
import chess.repository.ChessRepository;
import chess.repository.entity.BoardEntity;
import chess.repository.entity.ChessGameEntity;
import chess.repository.entity.GameRoomEntity;
import chess.service.util.BoardEntitiesToBoardConvertor;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private final ChessRepository chessRepository;

    public ChessService(ChessRepository chessRepository) {
        this.chessRepository = chessRepository;
    }

    public void createGameRoom(final String gameRoomId, final String name, final String password) {
        GameRoom gameRoom = new GameRoom(gameRoomId, name, password, ChessGame.createBasic());
        chessRepository.save(
                new GameRoomEntity(gameRoom),
                new ChessGameEntity(gameRoomId, gameRoom.getChessGame()),
                BoardEntity.generateBoardEntities(gameRoomId, gameRoom.getChessGame().getCurrentBoard())
        );
    }

    public GameRoom loadGameRoom(final String gameRoomId) {
        GameRoomEntity gameRoomEntity = chessRepository.loadGameRoom(gameRoomId);
        return new GameRoom(
                gameRoomEntity.getGameRoomId(),
                gameRoomEntity.getName(),
                gameRoomEntity.getPassword(),
                loadChessGame(gameRoomId)
        );
    }

    public List<GameRoom> loadAllGameRooms() {
        List<GameRoom> gameRooms = new ArrayList<>();
        for (GameRoomEntity gameRoomEntity : chessRepository.loadAllGameRoom()) {
            GameRoom gameRoom = loadGameRoom(gameRoomEntity.getGameRoomId());
            gameRooms.add(gameRoom);
        }
        return gameRooms;
    }

    private ChessGame loadChessGame(final String gameRoomId) {
        ChessGameEntity chessGameEntity = chessRepository.loadChessGame(gameRoomId);
        return new ChessGame(
                loadBoard(gameRoomId),
                new GameSwitch(chessGameEntity.getIsOn()),
                new Turn(Team.of(chessGameEntity.getTeamValueOfTurn()))
        );
    }

    public Board loadBoard(final String gameRoomId) {
        List<BoardEntity> boardEntities = chessRepository.loadBoard(gameRoomId);
        return BoardEntitiesToBoardConvertor.convert(boardEntities);
    }

    public void endChessGame(final String gameRoomId) {
        ChessGameEntity chessGameEntity = chessRepository.loadChessGame(gameRoomId);
        ChessGame chessGame = new ChessGame(
                loadBoard(gameRoomId),
                new GameSwitch(chessGameEntity.getIsOn()),
                new Turn(Team.of(chessGameEntity.getTeamValueOfTurn()))
        );
        chessGame.turnOff();
        chessRepository.updateChessGame(new ChessGameEntity(gameRoomId, chessGame));
    }

    public void deleteGameRoom(final String gameRoomId, final String password) {
        if (chessRepository.loadChessGame(gameRoomId).getIsOn()) {
            throw new IllegalStateException("[ERROR] 게임이 진행중인 체스방은 삭제할 수 없습니다.");
        }
        if (!chessRepository.loadGameRoom(gameRoomId).getPassword().equals(password)) {
            throw new IllegalArgumentException("[ERROR] 비밀번호가 일치하지 않아 삭제할 수 없습니다.");
        }
        chessRepository.delete(gameRoomId, password);
    }

    public void movePiece(
            final String gameRoomId,
            final char sourceColumnValue,
            final int sourceRowValue,
            final char targetColumnValue,
            final int targetRowValue
    ) {
        ChessGame chessGame = loadChessGame(gameRoomId);
        chessGame.move(sourceColumnValue, sourceRowValue, targetColumnValue, targetRowValue);
        chessRepository.updateChessGame(new ChessGameEntity(gameRoomId, chessGame));
        chessRepository.updateBoard(
                new BoardEntity(gameRoomId, sourceColumnValue, sourceRowValue, chessGame.getCurrentBoard())
        );
        chessRepository.updateBoard(
                new BoardEntity(gameRoomId, targetColumnValue, targetRowValue, chessGame.getCurrentBoard())
        );
    }

    public void resetChessRoom(final String gameRoomId) {
        GameRoomEntity gameRoomEntity = chessRepository.loadGameRoom(gameRoomId);
        chessRepository.delete(gameRoomId, gameRoomEntity.getPassword());
        createGameRoom(gameRoomId, gameRoomEntity.getName(), gameRoomEntity.getPassword());
    }
}
