package chess.service;

public enum TeamFormat {
    WHITE_TEAM("white", "white"),
    BLACK_TEAM("black", "black");

    private final String DtoFormat;
    private final String DaoFormat;

    TeamFormat(final String DtoFormat, final String DaoFormat) {
        this.DtoFormat = DtoFormat;
        this.DaoFormat = DaoFormat;
    }

    public String asDtoFormat() {
        return DtoFormat;
    }

    public String asDaoFormat() {
        return DaoFormat;
    }
}
