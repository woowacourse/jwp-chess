package chess.dto.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class RankingDTO implements Comparable {
    private final String nickname;
    private final int winCount;
    private final int loseCount;

    @Override
    public int compareTo(Object o) {
        RankingDTO rankingDTO = (RankingDTO) o;
        return Integer.compare(rankingDTO.winCount, this.winCount);
    }
}
