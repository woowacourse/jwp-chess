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
import chess.web.service.dto.MoveDto;
import chess.web.service.dto.ScoreDto;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChessService {

    private final PieceDao pieceDao;
    private final BoardDao boardDao;

    @Autowired
    public ChessService(PieceDao pieceDao, BoardDao boardDao) {
        this.pieceDao = pieceDao;
        this.boardDao = boardDao;
    }

    @Transactional
    public Board loadGame(Long boardId) {
        Room room = boardDao.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("없는 체스방입니다."));
        Turn turn = getTurn(room);

        List<Piece> pieces = pieceDao.findAllByBoardId(boardId);
        Board board = Board.create(Pieces.from(pieces), turn);

        if (board.isDeadKing() || pieces.isEmpty()) {
            return initBoard(boardId);
        }
        return board;
    }

    @Transactional
    public Board move(final MoveDto moveDto, final Long boardId) {
        Room room = boardDao.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("없는 체스방입니다."));
        Turn turn = getTurn(room);

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

    private Turn changeTurn(Turn turn, Long boardId) {
        Turn change = turn.change();
        boardDao.updateTurnById(boardId, change.getTeam().value());
        return change;
    }

    @Transactional
    public Board initBoard(Long boardId) {
        initTurn(boardId);
        initPiece(boardId);
        return loadGame(boardId);
    }

    private void initTurn(Long boardId) {
        boardDao.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("없는 체스방 id 입니다."));
        boardDao.updateTurnById(boardId, Turn.init().getTeam().value());
    }

    private void initPiece(Long boardId) {
        pieceDao.deleteAllByBoardId(boardId);

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

    @Transactional(readOnly = true)
    public ScoreDto getStatus(Long boardId) {
        List<Piece> found = pieceDao.findAllByBoardId(boardId);
        Pieces pieces = Pieces.from(found);

        double blackScore = pieces.getTotalScore(Team.BLACK);
        double whiteScore = pieces.getTotalScore(Team.WHITE);
        return new ScoreDto(blackScore, whiteScore);
    }

    @Transactional
    public Long createBoard(String title, String password) {
        Long id = boardDao.save(Turn.init().getTeam().value(), title, password);
        pieceDao.save(Pieces.createInit().getPieces(), id);
        return id;
    }

    @Transactional(readOnly = true)
    public List<Room> getRooms() {
        return boardDao.findAll();
    }

    @Transactional
    public boolean removeRoom(Long boardId, String password) {
        if (!canRemoveRoom(boardId, password)) {
            return false;
        }
        pieceDao.deleteAllByBoardId(boardId);
        boardDao.delete(boardId, password);
        return true;
    }

    private boolean canRemoveRoom(Long boardId, String password) {
        Optional<Room> room = boardDao.findById(boardId);
        if (room.isEmpty()) {
            throw new NoSuchElementException("삭제할 체스방이 없습니다.");
        }
        if (!matchPassword(room.get(), password)) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }
        return isFinishedChess(boardId, room.get());
    }

    private boolean isFinishedChess(Long boardId, Room room) {
        Turn turn = getTurn(room);
        Pieces pieces = Pieces.from(pieceDao.findAllByBoardId(boardId));
        Board board = Board.create(pieces, turn);

        return board.isDeadKing();
    }

    private boolean matchPassword(Room room, String password) {
        return password.equals(room.getPassword());
    }

    private Turn getTurn(Room room) {
        return new Turn(Team.from(room.getTurn()));
    }
}
