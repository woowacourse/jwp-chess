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
import chess.web.dto.DeleteDto;
import chess.web.dto.DeleteResultDto;
import chess.web.dto.MoveDto;
import chess.web.dto.MoveResultDto;
import chess.web.dto.PlayResultDto;
import chess.web.dto.ReadRoomResultDto;
import chess.web.dto.RoomDto;
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

    public RoomDto start(int roomId) {
        boolean roomExist = chessBoardDao.boardExistInRoom(roomId);
        if (!roomExist) {
            ChessGame chessGame = new ChessGame();
            chessGame.start();
            saveAll(chessGame, roomId);
        }
        return roomDao.isStartable(roomId);
    }

    public MoveResultDto move(MoveDto moveDto, int roomId) {
        ChessGame chessGame = getChessGame(roomId);
        String turn = chessGame.getTurn();

        Map<String, Piece> boardDto = createBoardDto(roomId);
        MoveResultDto moveResultDto = new MoveResultDto(boardDto);

        try {
            chessGame.move(Position.of(moveDto.getSource()), Position.of(moveDto.getTarget()));
            chessBoardDao.movePiece(moveDto, roomId);
            roomDao.changeTurn(roomId);
            if (isChessGameEnd(chessGame)) {
                moveResultDto.setIsGameOver(true);
                moveResultDto.setWinner(turn);
                roomDao.finish(roomId);
                return moveResultDto;
            }
        } catch (IllegalArgumentException e) {
            moveResultDto.setIsMovable(false);
        }

        return moveResultDto;
    }

    private Map<String, Piece> createBoardDto(int roomId) {
        Map<Position, Piece> board = findAllBoard(roomId);
        Map<String, Piece> boardDto = new HashMap<>();
        for (Position position : board.keySet()) {
            Piece piece = board.get(position);
            boardDto.put(position.toString(), piece);
        }
        return boardDto;
    }

    public PlayResultDto play(int roomId) {
        Map<String, Piece> boardDto = createBoardDto(roomId);
        return new PlayResultDto(boardDto, findTurn(roomId).name());
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

    public DeleteResultDto delete(int roomId, DeleteDto deleteDto) {
        return roomDao.delete(roomId, deleteDto);
    }
}
