package chess.repository;

import chess.dao.PieceDao;
import chess.dao.PieceDbDao;
import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.position.Position;
import chess.dto.BoardElementDto;
import chess.entity.PieceEntity;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class PieceRepository {

    private PieceDao pieceDao;

    public PieceRepository(@Qualifier("PieceDbDao") PieceDao pieceDao) {
        this.pieceDao = pieceDao;
    }

    public void saveAllPiece(int gameId, Map<Position, Piece> boardElements) {
        List<PieceEntity> pieceEntities = convertToEntities(gameId, boardElements);
        for (PieceEntity pieceEntity : pieceEntities) {
            pieceDao.insert(pieceEntity);
        }
    }

    public List<BoardElementDto> findAll(int gameId) {
        List<PieceEntity> pieceEntities = pieceDao.findAll(PieceEntity.of(gameId));
        return convertToBoardElementDtos(pieceEntities);
    }

    public void deleteAllPiece(int gameId) {
        pieceDao.deleteAll(PieceEntity.of(gameId));
    }

    public void updatePiecePosition(int gameId, String from, String to) {
        PieceEntity pieceEntity = pieceDao.find(PieceEntity.of(gameId, from));
        pieceDao.update(PieceEntity.of(gameId, pieceEntity.getPieceName(), pieceEntity.getPieceColor(), from),
                PieceEntity.of(gameId, pieceEntity.getPieceName(), pieceEntity.getPieceColor(), to));
    }

    public Board getBoard(int gameId) {
        return convertToBoard(convertToBoardElementDtos(pieceDao.findAll(PieceEntity.of(gameId))));
    }

    public void deletePiece(int gameId, String position) {
        pieceDao.delete(PieceEntity.of(gameId, position));
    }

    private Board convertToBoard(final List<BoardElementDto> boardDatas) {
        Map<Position, Piece> board = boardDatas.stream()
                .collect(Collectors.toMap(it -> Position.from(it.getPosition()),
                        it -> PieceFactory.of(it.getPieceName(), it.getPieceColor())));
        return new Board(() -> board);
    }

    private List<BoardElementDto> convertToBoardElementDtos(List<PieceEntity> pieceEntities) {
        return pieceEntities.stream()
                .map(it -> new BoardElementDto(it.getPieceName(), it.getPieceColor(), it.getPosition()))
                .collect(Collectors.toList());
    }

    private List<PieceEntity> convertToEntities(int gameId, Map<Position, Piece> boardElements) {
        return boardElements.entrySet()
                .stream()
                .map(it -> convertToEntity(gameId, it.getKey(), it.getValue()))
                .collect(Collectors.toList());
    }

    private PieceEntity convertToEntity(int gameId, Position position, Piece piece) {
        return PieceEntity.of(gameId,
                piece.getNotation().name(),
                piece.getColor().name(),
                position.getFile().name() + position.getRankNumber());
    }
}
