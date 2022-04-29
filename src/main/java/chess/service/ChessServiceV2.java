package chess.service;

import chess.dao.RoomDao;
import chess.dao.SquareDao;
import chess.domain.chessboard.ChessBoard;
import chess.domain.piece.generator.NormalPiecesGenerator;
import chess.entity.Square;
import java.util.List;
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
        roomDao.updateStateById(roomId, "Ready");

        return roomId;
    }
}
