package chess.web.service;

import chess.domain.board.Board;
import chess.domain.board.Team;
import chess.domain.board.Turn;
import chess.domain.board.piece.Empty;
import chess.domain.board.piece.Piece;
import chess.domain.board.piece.Pieces;
import chess.domain.board.piece.position.Position;
import chess.domain.entity.Room;
import chess.web.controller.dto.MoveDto;
import chess.web.controller.dto.ScoreDto;
import chess.web.dao.PieceDao;
import chess.web.dao.RoomDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class ChessService {

    private final RoomDao roomDao;
    private final PieceDao pieceDao;

    @Autowired
    public ChessService(RoomDao roomDao, PieceDao pieceDao) {
        this.roomDao = roomDao;
        this.pieceDao = pieceDao;
    }

    public Board loadGame(Long boardId) {
        Turn turn = roomDao.findTurnById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("없는 차례입니다."));

        List<Piece> pieces = pieceDao.findAllByBoardId(boardId);
        return Board.create(Pieces.from(pieces), turn);
    }

    @Transactional
    public Board move(final MoveDto moveDto, final Long boardId) {
        Turn turn = roomDao.findTurnById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("없는 정보입니다."));
        Board board = Board.create(Pieces.from(pieceDao.findAllByBoardId(boardId)), turn);
        Pieces pieces = board.getPieces();
        Piece piece = pieces.findByPosition(Position.from(moveDto.getFrom()));
        board.move(List.of(moveDto.getFrom(), moveDto.getTo()), turn);
        Turn changedTurn = updatePieces(moveDto, turn, piece, boardId);
        return Board.create(pieces, changedTurn);
    }

    private Turn updatePieces(MoveDto moveDto, Turn turn, Piece piece, final Long boardId) {
        Turn changedTurn = changeTurn(turn, boardId);
        Empty empty = new Empty(Position.from(moveDto.getFrom()));
        pieceDao.updatePieceByPositionAndBoardId(empty.getType(), empty.getTeam().value(), moveDto.getFrom(), boardId);
        pieceDao.updatePieceByPositionAndBoardId(piece.getType(), piece.getTeam().value(), moveDto.getTo(), boardId);
        return changedTurn;
    }

    private Turn changeTurn(Turn turn, Long id) {
        Turn change = turn.change();
        roomDao.updateTurnById(id, change.getTeam().value());
        return change;
    }

    @Transactional
    public Board initBoard(Long boardId) {
        Pieces pieces = Pieces.createInit();
        Board board = Board.create(pieces, Turn.init());
        for (Piece piece : pieces.getPieces()) {
            pieceDao.updatePieceByPositionAndBoardId(piece.getType(), piece.getTeam().value(), piece.getPosition().name(), boardId);
        }
        roomDao.updateTurnById(boardId, Turn.init().getTeam().value());
        return board;
    }

    public ScoreDto getStatus(Long boardId) {
        List<Piece> found = pieceDao.findAllByBoardId(boardId);
        Pieces pieces = Pieces.from(found);

        double blackScore = pieces.getTotalScore(Team.BLACK);
        double whiteScore = pieces.getTotalScore(Team.WHITE);
        return new ScoreDto(blackScore, whiteScore);
    }
}
