package chess.web.service;

import chess.domain.game.ChessGame;
import chess.domain.game.state.ChessBoard;
import chess.domain.game.state.Player;
import chess.domain.game.state.RunningGame;
import chess.domain.piece.Piece;
import chess.domain.piece.position.Position;
import chess.domain.piece.property.Color;
import chess.web.dao.ChessBoardDao;
import chess.web.dao.RoomDao;
import chess.web.dto.CreateRoomRequestDto;
import chess.web.dto.CreateRoomResultDto;
import chess.web.dto.MoveDto;
import chess.web.dto.MoveResultDto;
import chess.web.dto.PlayResultDto;
import chess.web.dto.ReadRoomResultDto;
import chess.web.dto.StartResultDto;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {

    private final ChessBoardDao chessBoardDao;
    private final RoomDao roomDao;

    public ChessGameService(ChessBoardDao chessBoardDao, RoomDao playerDao) {
        this.chessBoardDao = chessBoardDao;
        this.roomDao = playerDao;
    }

    public StartResultDto start(int roomId) {
        ChessGame chessGame = new ChessGame();
        chessGame.start();

        removeAll(roomId);
        saveAll(chessGame, roomId);

        return new StartResultDto(roomId);
    }

    public MoveResultDto move(MoveDto moveDto, int roomId) {
        ChessGame chessGame = getChessGame(roomId);
        String turn = chessGame.getTurn();

        Map<Position, Piece> board = findAllBoard(roomId);
        Map<String, Piece> boardDto = new HashMap<>();
        for (Position position : board.keySet()) {
            Piece piece = board.get(position);
            boardDto.put(position.toString(), piece);
        }

        MoveResultDto moveResultDto = new MoveResultDto(boardDto);

        try {
            chessGame.move(Position.of(moveDto.getSource()), Position.of(moveDto.getTarget()));
            removeAll(roomId);
            if (isChessGameEnd(chessGame)) {
                moveResultDto.setIsGameOver(true);
                moveResultDto.setWinner(turn);
                return moveResultDto;
            }
            saveAll(chessGame, roomId);
        } catch (IllegalArgumentException e) {
            moveResultDto.setIsMovable(false);
        }

        return moveResultDto;
    }

    public PlayResultDto play(int roomId) {
        Map<Position, Piece> board = findAllBoard(roomId);
        if (board.isEmpty()) {
            start(roomId);
        }

        Map<String, Piece> boardDto = new HashMap<>();
        for (Position position : board.keySet()) {
            Piece piece = board.get(position);
            boardDto.put(position.toString(), piece);
        }
        return new PlayResultDto(boardDto, findTurn(roomId).name());
    }

    private void removeAll(int roomId) {
        chessBoardDao.deleteAll(roomId);
        //roomDao.deleteAll(roomId);
    }

    private void saveAll(ChessGame chessGame, int roomId) {
        Map<Position, Piece> chessBoard = chessGame.getBoard();
        for (Position position : chessBoard.keySet()) {
            chessBoardDao.save(position, chessBoard.get(position), roomId);
        }
        roomDao.saveTurn(Color.of(chessGame.getTurn()), roomId);
    }

    public ChessGame getChessGame(int roomId) {
        return ChessGame.of(new RunningGame(ChessBoard.of(findAllBoard(roomId)), findTurn(roomId)));
    }

    private Map<Position, Piece> findAllBoard(int roomId) {
        return chessBoardDao.findAll(roomId);
    }

    private Player findTurn(int roomId) {
        return roomDao.getPlayer(roomId);
    }

    private boolean isChessGameEnd(ChessGame chessGame) {
        return chessGame.isFinished();
    }

    public CreateRoomResultDto createRoom(CreateRoomRequestDto createRoomRequestDto) {
        return new CreateRoomResultDto(roomDao.createRoom(createRoomRequestDto));
    }

    public ReadRoomResultDto findAllRooms() {
        return roomDao.findAll();
    }
}
