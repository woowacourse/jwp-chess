package chess.service;

import chess.dao.RoomDao;
import chess.dao.SquareDao;
import chess.domain.chessboard.ChessBoard;
import chess.domain.command.GameCommand;
import chess.domain.game.ChessGame;
import chess.domain.piece.EmptyPiece;
import chess.domain.piece.Piece;
import chess.domain.piece.generator.NormalPiecesGenerator;
import chess.domain.position.Position;
import chess.domain.state.State;
import chess.entity.Room;
import chess.entity.Square;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChessServiceV2 {

    private final RoomDao roomDao;
    private final SquareDao squareDao;

    public ChessServiceV2(RoomDao roomDao, SquareDao squareDao) {
        this.roomDao = roomDao;
        this.squareDao = squareDao;
    }

    public Long insertRoom(String title, String password) {
        validateValueIsBlank(title, password);
        final Long roomId = roomDao.insertRoom(title, password);

        return roomId;
    }

    private void validateValueIsBlank(String title, String password) {
        if (title.isBlank()) {
            throw new IllegalArgumentException("제목이 빈칸입니다.");
        }
        if (password.isBlank()) {
            throw new IllegalArgumentException("비밀번호가 빈칸입니다");
        }
    }

    @Transactional
    public Long insertBoard(Long roomId) {
        ChessBoard chessBoard = new ChessBoard(new NormalPiecesGenerator());
        List<Square> board = chessBoard.getPieces().entrySet().stream()
                .map(entry -> new Square(roomId, entry.getKey().toString(),
                        entry.getValue().getSymbol().name(), entry.getValue().getColor().name()))
                .collect(Collectors.toList());

        squareDao.insertSquareAll(roomId, board);
        roomDao.updateStateById(roomId, "WhiteRunning");

        return roomId;
    }

    @Transactional
    public void updateSquares(Long roomId, String from, String to) {
        ChessGame chessGame = findChessGame(roomId);
        playChessGame(chessGame, from, to);

        String source = Position.of(from).toString();
        String target = Position.of(to).toString();

        Map<String, Piece> pieces = chessGame.getChessBoard().toMap();
        squareDao.updateSquare(new Square(roomId, target,
                pieces.get(target).getSymbol().toString(), pieces.get(target).getColor().toString()));
        squareDao.updateSquare(new Square(roomId, source,
                EmptyPiece.getInstance().getSymbol().toString(), EmptyPiece.getInstance().getColor().toString()));
    }

    private ChessGame findChessGame(Long roomId) {
        final Room room = roomDao.findRoomById(roomId);
        final List<Square> squares = squareDao.findSquareAllById(roomId);

        final Map<Position, Piece> board = new HashMap<>();
        for (Square square : squares) {
            board.put(Position.of(square.getPosition()), Piece.of(square.getColor(), square.getSymbol()));
        }

        ChessGame chessGame = new ChessGame(State.of(room.getState()), new ChessBoard(board));
        return chessGame;
    }

    private void playChessGame(ChessGame chessGame, String from, String to) {
        chessGame.playGameByCommand(GameCommand.of("move", from, to));
        chessGame.isEndGameByPiece();
    }

    public Long updateStateEnd(Long roomId) {
        final Long updateRoomId = roomDao.updateStateById(roomId, "Finished");
        return updateRoomId;
    }

    public Long deleteRoom(Long roomId, String password) {
        Room room = roomDao.findRoomById(roomId);
        if (!room.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        if (room.getState().equals("WhiteRunning") || room.getState().equals("BlackRunning")) {
            throw new IllegalStateException("게임이 실행중일 경우 게임을 삭제할 수 없습니다.");
        }
        return roomDao.deleteRoom(roomId);
    }
}
