package chess.web.service;

import chess.domain.game.ChessGame;
import chess.domain.game.state.ChessBoard;
import chess.domain.game.state.Player;
import chess.domain.game.state.RunningGame;
import chess.domain.piece.Piece;
import chess.domain.piece.position.Position;
import chess.domain.piece.property.Color;
import chess.domain.room.RoomName;
import chess.domain.room.RoomPassword;
import chess.web.dao.ChessBoardDao;
import chess.web.dao.PlayerDao;
import chess.web.dao.RoomDao;
import chess.web.dto.CreateRoomDto;
import chess.web.dto.MoveDto;
import chess.web.dto.PlayResultDto;
import chess.web.dto.RoomDto;
import chess.web.dto.ScoreDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {

    private final ChessBoardDao chessBoardDao;
    private final PlayerDao playerDao;
    private final RoomDao roomDao;

    public ChessGameService(ChessBoardDao chessBoardDao, PlayerDao playerDao, RoomDao roomDao) {
        this.chessBoardDao = chessBoardDao;
        this.playerDao = playerDao;
        this.roomDao = roomDao;
    }

    public ChessGame start(int id) {
        ChessGame chessGame = new ChessGame();
        chessGame.start();

        deleteById(id);
        saveById(chessGame, id);

        return chessGame;
    }

    public PlayResultDto move(MoveDto moveDto) {
        final int id = moveDto.getId();
        ChessGame chessGame = getOneChessGame(id);
        String turn = chessGame.getTurn();

        chessGame.move(Position.of(moveDto.getSource()), Position.of(moveDto.getTarget()));
        Map<Position, Piece> board = chessGame.getBoard();
        if (isChessGameEnd(chessGame)) {
            return PlayResultDto.of(toBoardDto(board), turn, isChessGameEnd(chessGame));
        }

        deleteById(id);
        saveById(chessGame, id);
        return PlayResultDto.of(toBoardDto(board), findOneTurn(id).name(), isChessGameEnd(chessGame));
    }

    public PlayResultDto play(int id) {
        if (findOneBoard(id).isEmpty()) {
            start(id);
        }
        return PlayResultDto.of(toBoardDto(findOneBoard(id)), findOneTurn(id).name(), false);
    }

    public ScoreDto status(int id) {
        ChessBoard board = ChessBoard.of(findOneBoard(id));
        Map<Color, Double> score = board.computeScore();
        deleteById(id);
        return new ScoreDto(score.get(Color.WHITE), score.get(Color.BLACK));
    }

    public RoomDto createRoom(CreateRoomDto createRoomDto) {
        String name = createRoomDto.getRoomName();
        String password = createRoomDto.getPassword();
        int roomNumber = roomDao.save(RoomName.of(name), RoomPassword.of(password));

        return RoomDto.of(roomNumber, RoomName.of(name));
    }

    public List<RoomDto> loadChessGames() {
        return roomDao.findAll();
    }

    public ChessGame getOneChessGame(int id) {
        return ChessGame.of(new RunningGame(ChessBoard.of(findOneBoard(id)), findOneTurn(id)));
    }

    private Map<Position, Piece> findOneBoard(int id) {
        return chessBoardDao.findById(id);
    }

    private Player findOneTurn(int id) {
        return playerDao.findById(id);
    }

    private boolean isChessGameEnd(ChessGame chessGame) {
        return chessGame.isFinished();
    }

    private Map<String, Piece> toBoardDto(Map<Position, Piece> board) {
        Map<String, Piece> boardDto = new HashMap<>();
        for (Position position : board.keySet()) {
            Piece piece = board.get(position);
            boardDto.put(position.toString(), piece);
        }
        return boardDto;
    }

    private void deleteById(int id) {
        chessBoardDao.deleteById(id);
        playerDao.deleteById(id);
    }

    private void saveById(ChessGame chessGame, int id) {
        Map<Position, Piece> chessBoard = chessGame.getBoard();
        for (Position position : chessBoard.keySet()) {
            chessBoardDao.saveById(id, position, chessBoard.get(position));
        }
        playerDao.saveById(id, Color.of(chessGame.getTurn()));
    }

    public void deleteRoomById(int id) {
        roomDao.deleteById(id);
    }

    public boolean confirmPassword(int id, String password) {
        return roomDao.confirmPassword(id, password);
    }
}
