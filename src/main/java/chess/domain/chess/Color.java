package chess.domain.chess;

public enum Color {
    BLACK {
        @Override
        public Color next() {
            return WHITE;
        }
    }, WHITE {
        @Override
        public Color next() {
            return BLACK;
        }
    }, BLANK {
        @Override
        public Color next() {
            throw new UnsupportedOperationException("BLANK는 컬러를 변경할 수 없습니다.");
        }
    };

    public boolean isBlack() {
        return this == BLACK;
    }

    public boolean isWhite() {
        return this == WHITE;
    }

    public abstract Color next();
}
