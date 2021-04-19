package chess.domain.position.type;

import chess.domain.piece.type.Direction;
import java.util.Arrays;

public enum File {
    A(1),
    B(2),
    C(3),
    D(4),
    E(5),
    F(6),
    G(7),
    H(8);

    private final int order;

    File(int order) {
        this.order = order;
    }

    public static File of(String fileInput) {
        return File.valueOf(fileInput.toUpperCase());
    }

    public File getMovedFile(Direction direction) {
        int movedX = order + direction.getX();
        return findFileByOrder(movedX);
    }

    private static File findFileByOrder(int order) {
        return Arrays.stream(values())
            .filter(file -> file.getOrder() == order)
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("잘못된 file값 입니다."));
    }

    public boolean isSameAs(File destinationFile) {
        return this == destinationFile;
    }

    public int getOrder() {
        return order;
    }
}
