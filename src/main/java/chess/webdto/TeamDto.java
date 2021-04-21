package chess.webdto;

public enum TeamDto {
    WHITE_TEAM("white"),
    BLACK_TEAM("black");

    private final String team;

    TeamDto(final String team) {
        this.team = team;
    }

    public String team() {
        return team;
    }

    public boolean isEqual(String word){
       return this.team.equals(word);
    }
}
