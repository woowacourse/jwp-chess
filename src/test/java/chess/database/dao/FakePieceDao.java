package chess.database.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import chess.database.entity.PieceEntity;
import chess.database.entity.PointEntity;

public class FakePieceDao implements PieceDao {

    private final List<FakeRow> memoryDatabase;

    public FakePieceDao() {
        this.memoryDatabase = new ArrayList<>();
    }

    private static class FakeRow {
        private final String pieceType;
        private final String pieceColor;
        private Integer horizontalIndex;
        private Integer verticalIndex;
        private final Long gameId;

        public FakeRow(String pieceType, String pieceColor, Integer horizontalIndex, Integer verticalIndex,
            Long gameId) {
            this.pieceType = pieceType;
            this.pieceColor = pieceColor;
            this.horizontalIndex = horizontalIndex;
            this.verticalIndex = verticalIndex;
            this.gameId = gameId;
        }

        public String getPieceType() {
            return pieceType;
        }

        public String getPieceColor() {
            return pieceColor;
        }

        public Integer getHorizontalIndex() {
            return horizontalIndex;
        }

        public Integer getVerticalIndex() {
            return verticalIndex;
        }

        public Long getGameId() {
            return gameId;
        }

        public void setHorizontalIndex(Integer horizontalIndex) {
            this.horizontalIndex = horizontalIndex;
        }

        public void setVerticalIndex(Integer verticalIndex) {
            this.verticalIndex = verticalIndex;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            FakeRow fakeRow = (FakeRow)o;
            return Objects.equals(pieceType, fakeRow.pieceType) && Objects.equals(pieceColor,
                fakeRow.pieceColor) && Objects.equals(horizontalIndex, fakeRow.horizontalIndex)
                && Objects.equals(verticalIndex, fakeRow.verticalIndex) && Objects.equals(gameId,
                fakeRow.gameId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(pieceType, pieceColor, horizontalIndex, verticalIndex, gameId);
        }

    }

    @Override
    public void saveBoard(List<PieceEntity> entities) {
        for (PieceEntity entity : entities) {
            memoryDatabase.add(new FakeRow(
                entity.getPieceType(),
                entity.getPieceColor(),
                entity.getHorizontalIndex(),
                entity.getVerticalIndex(),
                entity.getGameId()));
        }
    }

    @Override
    public List<PieceEntity> findBoardByGameId(Long gameId) {
        return memoryDatabase.stream()
            .filter(row -> row.getGameId().equals(gameId))
            .map(row -> new PieceEntity(
                row.getPieceType(),
                row.getPieceColor(),
                row.getVerticalIndex(),
                row.getHorizontalIndex(),
                row.getGameId()))
            .collect(Collectors.toList());
    }

    @Override
    public void deletePiece(PointEntity pointEntity, Long gameId) {
        final FakeRow fakeRow = findFakeRow(pointEntity);
        memoryDatabase.remove(fakeRow);
    }

    private FakeRow findFakeRow(PointEntity pointEntity) {
        return memoryDatabase.stream()
            .filter(row -> row.getHorizontalIndex().equals(pointEntity.getHorizontalIndex()) &&
                row.getVerticalIndex().equals(pointEntity.getVerticalIndex()))
            .findAny()
            .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public void updatePiece(PointEntity sourceEntity, PointEntity destinationEntity, Long gameId) {
        final FakeRow sourceRow = findFakeRow(sourceEntity);
        final FakeRow destinationRow = findFakeRow(destinationEntity);
        memoryDatabase.remove(destinationRow);
        sourceRow.setHorizontalIndex(destinationRow.getHorizontalIndex());
        sourceRow.setVerticalIndex(destinationRow.getVerticalIndex());
    }

}
