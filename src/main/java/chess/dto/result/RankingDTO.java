package chess.dto.result;

public final class RankingDTO implements Comparable {
    private final String nickname;
    private final int winCount;
    private final int loseCount;

    public RankingDTO(final String nickname, final int winCount, final int loseCount) {
        this.nickname = nickname;
        this.winCount = winCount;
        this.loseCount = loseCount;
    }

    public String getNickname() {
        return nickname;
    }

    public int getWinCount() {
        return winCount;
    }

    public int getLoseCount() {
        return loseCount;
    }

    @Override
    public int compareTo(Object o) {
        RankingDTO rankingDTO = (RankingDTO) o;
        return Integer.compare(rankingDTO.winCount, this.winCount);
    }
}
