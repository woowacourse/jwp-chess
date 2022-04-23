package chess.web.service;

import chess.board.Board;
import chess.board.Team;
import chess.board.Turn;
import chess.board.piece.Empty;
import chess.board.piece.Piece;
import chess.board.piece.Pieces;
import chess.board.piece.position.Position;
import chess.web.dao.BoardDao;
import chess.web.dao.BoardDaoImpl;
import chess.web.dao.PieceDao;
import chess.web.dao.PieceDaoImpl;
import chess.web.service.dto.MoveDto;
import chess.web.service.dto.ScoreDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChessService {

    private BoardDao boardDao;
    private PieceDao pieceDao;

    public ChessService() {
    }

    @Autowired
    public ChessService(BoardDao boardDao, PieceDao pieceDao) {
        this.boardDao = boardDao;
        this.pieceDao = pieceDao;
    }

    public Board loadGame(Long boardId) {
        Turn turn = boardDao.findTurnById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("없는 차례입니다."));

        List<Piece> pieces = pieceDao.findAllByBoardId(boardId);
        return Board.create(Pieces.from(pieces), turn);
    }

    public Board move(final MoveDto moveDto, final Long boardId) {
        Turn turn = boardDao.findTurnById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("없는 정보입니다."));

        Board board = Board.create(Pieces.from(pieceDao.findAllByBoardId(boardId)), turn);
        Pieces pieces = board.getPieces();

        Piece piece = pieces.findByPosition(Position.from(moveDto.getFrom()));
        board.move(List.of(moveDto.getFrom(), moveDto.getTo()), turn);
        Turn changedTurn = updatePieces(moveDto, turn, piece, boardId);

        return Board.create(pieces, changedTurn);
    }

    private Turn updatePieces(MoveDto moveDto, Turn turn, Piece piece, final Long boardId) {
        Turn changedTurn = changeTurn(turn);
        Empty empty = new Empty(Position.from(moveDto.getFrom()));
        pieceDao.updatePieceByPositionAndBoardId(empty.getType(), empty.getTeam().value(), moveDto.getFrom(), boardId);
        pieceDao.updatePieceByPositionAndBoardId(piece.getType(), piece.getTeam().value(), moveDto.getTo(), boardId);
        return changedTurn;
    }

    private Turn changeTurn(Turn turn) {
        Turn change = turn.change();
        boardDao.updateTurnById(1L, change.getTeam().value());
        return change;
    }

    public Board initBoard(Long boardId) {
        Pieces pieces = Pieces.createInit();
        Turn turn = Turn.init();

        pieceDao.deleteByBoardId(boardId);
        boardDao.deleteById(boardId);

        insertOrUpdateTurn(boardId, turn);
        for (Piece piece : pieces.getPieces()) {
            insertOrUpdatePiece(boardId, piece);
        }

        Pieces savedPieces = Pieces.from(pieceDao.findAllByBoardId(boardId));
        return Board.create(savedPieces, turn);
    }

    private void insertOrUpdatePiece(Long boardId, Piece piece) {
        String position = piece.getPosition().name();
        String type = piece.getType();
        String team = piece.getTeam().value();

        if (pieceDao.findByPositionAndBoardId(position, boardId).isPresent()) {
            pieceDao.updatePieceByPositionAndBoardId(type, team, position, boardId);
            return;
        }
        pieceDao.save(piece, boardId);
    }

    private void insertOrUpdateTurn(Long boardId, Turn turn) {
        if (boardDao.findTurnById(boardId).isPresent()) {
            boardDao.updateTurnById(boardId, turn.getTeam().value());
            return;
        }
        boardDao.save(boardId, turn);
    }

    public ScoreDto getStatus(Long boardId) {
        List<Piece> found = pieceDao.findAllByBoardId(boardId);
        Pieces pieces = Pieces.from(found);

        double blackScore = pieces.getTotalScore(Team.BLACK);
        double whiteScore = pieces.getTotalScore(Team.WHITE);
        return new ScoreDto(blackScore, whiteScore);
    }
}
