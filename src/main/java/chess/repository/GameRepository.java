package chess.repository;

import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.BoardDto;
import chess.dto.GameStateDto;
import chess.dto.PieceDto;
import chess.service.PieceFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepository {

    private final BoardDao boardDao;
    private final PieceDao pieceDao;

    public GameRepository(BoardDao boardDao, PieceDao pieceDao) {
        this.boardDao = boardDao;
        this.pieceDao = pieceDao;
    }

    public BoardDto saveNewGame(int roomId, Board board) {
        deleteOldGame(roomId);
        int boardId = saveGame(roomId, board);
        return gameStateAndPieces(boardId);
    }

    private int saveGame(int roomId, Board board) {
        int boardId = boardDao.save(roomId, getGameStateDto(board));
        pieceDao.saveAll(boardId, board.getPieces());
        return boardId;
    }

    private void deleteOldGame(int roomId) {
        boardDao.deleteByRoom(roomId);
    }

    public int findBoardIdByRoomId(int roomId) {
        return boardDao.findBoardIdByRoom(roomId).get();
    }

    public Board findBoardByRoomId(int roomId) {
        int boardId = boardDao.findBoardIdByRoom(roomId).get();

        Map<Position, Piece> pieces = new HashMap<>();
        for (PieceDto pieceDto : pieceDao.findAll(boardId)) {
            pieces.put(Position.of(pieceDto.getPosition()), PieceFactory.build(pieceDto));
        }
        Board board = new Board(() -> pieces, boardDao.getTurn(boardId));
        return board;
    }

    public void updateBoardState(Board board, int boardId) {
        boardDao.updateState(boardId, getGameStateDto(board));
    }

    public Board loadBoard(int boardId) {
        Map<Position, Piece> pieces = new HashMap<>();
        for (PieceDto pieceDto : pieceDao.findAll(boardId)) {
            pieces.put(Position.of(pieceDto.getPosition()), PieceFactory.build(pieceDto));
        }
        Board board = new Board(() -> pieces, boardDao.getTurn(boardId));
        return board;
    }

    public void savePiece(int boardId, String position, PieceDto pieceDto) {
        pieceDao.save(boardId, position, pieceDto);
    }

    public Optional<PieceDto> findPiece(int boardId, String position) {
        return pieceDao.findOne(boardId, position);
    }

    public void updatePiece(int boardId, String position, PieceDto pieceDto) {
        pieceDao.updateOne(boardId, position, pieceDto);
    }

    public void deletePiece(int boardId, String position) {
        pieceDao.deleteOne(boardId, position);
    }

    private GameStateDto getGameStateDto(Board board) {
        return GameStateDto.from(board);
    }

    private BoardDto gameStateAndPieces(int boardId) {
        Board board = loadBoard(boardId);
        return new BoardDto(boardId, board);
    }

    public void movePiece(int boardId, Board board, String source, String target) {
        PieceDto pieceDto = findPiece(boardId, source).get();
        deletePiece(boardId, source);
        Optional<PieceDto> targetPieceDto = findPiece(boardId, target);
        if (targetPieceDto.isPresent()) {
            updatePiece(boardId, target, pieceDto);
            return;
        }
        savePiece(boardId, target, pieceDto);
        updateBoardState(board, boardId);
    }
}
