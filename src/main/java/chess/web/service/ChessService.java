package chess.web.service;

import chess.board.Board;
import chess.board.Room;
import chess.board.Team;
import chess.board.Turn;
import chess.board.piece.Empty;
import chess.board.piece.Piece;
import chess.board.piece.Pieces;
import chess.board.piece.position.Position;
import chess.web.dao.BoardDao;
import chess.web.dao.PieceDao;
import chess.web.dao.RoomDao;
import chess.web.service.dto.MoveDto;
import chess.web.service.dto.ScoreDto;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private final BoardDao boardDao;
    private final PieceDao pieceDao;
    private final RoomDao roomDao;

    @Autowired
    public ChessService(BoardDao boardDao, PieceDao pieceDao, RoomDao roomDao) {
        this.boardDao = boardDao;
        this.pieceDao = pieceDao;
        this.roomDao = roomDao;
    }

    public Board loadGame(Long boardId) {
        Turn turn = boardDao.findTurnById(boardId)
                .orElseThrow(() -> new NoSuchElementException("없는 차례입니다."));

        List<Piece> pieces = pieceDao.findAllByBoardId(boardId);
        Board board = Board.create(Pieces.from(pieces), turn);

        if (board.isDeadKing() || pieces.isEmpty()) {
            return initBoard(boardId);
        }
        return board;
    }

    public Board move(final MoveDto moveDto, final Long boardId) {
        Turn turn = boardDao.findTurnById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("없는 정보입니다."));

        Board board = Board.create(Pieces.from(pieceDao.findAllByBoardId(boardId)), turn);
        Pieces pieces = board.getPieces();

        Piece piece = pieces.findByPosition(Position.from(moveDto.getFrom()));
        try {
            board.move(List.of(moveDto.getFrom(), moveDto.getTo()), turn);
        } catch (IllegalArgumentException exception) {
            return board;
        }
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
        boardDao.update(1L, change);
        return change;
    }

    public Board initBoard(Long boardId) {
        initTurn(boardId);
        initPiece(boardId);
        return loadGame(boardId);
    }

    private void initTurn(Long boardId) {
        boardDao.findTurnById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("없는 체스방 id 입니다."));
        boardDao.update(boardId, Turn.init());
    }

    private void initPiece(Long boardId) {
        pieceDao.deleteByBoardId(boardId);

        Pieces pieces = Pieces.createInit();
        for (Piece piece : pieces.getPieces()) {
            insertOrUpdatePiece(boardId, piece);
        }
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

    public ScoreDto getStatus(Long boardId) {
        List<Piece> found = pieceDao.findAllByBoardId(boardId);
        Pieces pieces = Pieces.from(found);

        double blackScore = pieces.getTotalScore(Team.BLACK);
        double whiteScore = pieces.getTotalScore(Team.WHITE);
        return new ScoreDto(blackScore, whiteScore);
    }

    public Long createGame() {
        Long boardId = boardDao.save(Turn.init());
        pieceDao.save(Pieces.createInit().getPieces(), boardId);
        return boardId;
    }

    public Long createRoom(Long boardId, String title, String password) {
        return roomDao.save(boardId, title, password);
    }

    public List<Room> getRooms() {
        return roomDao.findAll();
    }

    public boolean removeRoom(Long boardId, String password) {
        if (!hasRoom(boardId) || isRunningChess(boardId)) {
            return false;
        }
        pieceDao.deleteByBoardId(boardId);
        roomDao.delete(boardId, password);
        boardDao.deleteById(boardId);
        return true;
    }

    private boolean hasRoom(Long boardId) {
        return roomDao.findByBoardId(boardId).isPresent();
    }

    private boolean isRunningChess(Long boardId) {
        long kingCount = pieceDao.findAllByBoardId(boardId).stream()
                .filter(Piece::isKing)
                .count();

        return kingCount == 2;
    }
}
