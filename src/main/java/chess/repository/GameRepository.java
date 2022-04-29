package chess.repository;

import chess.domain.Color;
import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.service.PieceFactory;
import chess.web.dto.BoardDto;
import chess.web.dto.GameStateDto;
import chess.web.dto.PieceDto;
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

    public BoardDto saveGame(int roomId, Board board) {
        int boardId = boardDao.save(roomId, getGameStateDto(board));
        pieceDao.saveAll(boardId, board.getPieces());
        return gameStateAndPieces(boardId);
    }

    public int findBoardIdByRoom(int roomId) {
        return boardDao.findBoardIdByRoom(roomId).get();
    }

    public Color getBoardColor(int boardId) {
        return boardDao.getTurn(boardId);
    }

    public void updateBoardState(Board board, int boardId) {
        boardDao.updateState(boardId, getGameStateDto(board));
    }

    public void deleteOldBoard(int roomId) {
        boardDao.deleteByRoom(roomId);
    }

    public Board loadBoard(int boardId) {
        Map<Position, Piece> pieces = new HashMap<>();
        for (PieceDto pieceDto : pieceDao.findAll(boardId)) {
            pieces.put(Position.of(pieceDto.getPosition()), PieceFactory.build(pieceDto));
        }
        Board board = new Board(() -> pieces);
        board.loadTurn(boardDao.getTurn(boardId));
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
}
