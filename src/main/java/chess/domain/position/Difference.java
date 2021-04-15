package chess.domain.position;

import java.util.List;

public final class Difference {

    private final int fileDegree;
    private final int rankDegree;

    public Difference(final int fileDegree, final int rankDegree) {
        this.fileDegree = fileDegree;
        this.rankDegree = rankDegree;
    }

    public Difference(final List<Integer> positionDifference) {
        fileDegree = positionDifference.get(0);
        rankDegree = positionDifference.get(1);
    }

    public boolean isSameAbsoluteValue() {
        return Math.abs(fileDegree) == Math.abs(rankDegree);
    }

    public boolean hasZeroValue() {
        return fileDegree == 0 || rankDegree == 0;
    }

    public boolean allZeroValue() {
        return fileDegree == 0 && rankDegree == 0;
    }

    public int biggerAbsoluteValue() {
        return Math.max(Math.abs(fileDegree), Math.abs(rankDegree));
    }

    public Difference makeUnitLength() {
        final int absoluteValue = biggerAbsoluteValue();
        return new Difference(fileDegree / biggerAbsoluteValue(), rankDegree / biggerAbsoluteValue());
    }

    public int fileDegree() {
        return fileDegree;
    }

    public int rankDegree() {
        return rankDegree;
    }
}
