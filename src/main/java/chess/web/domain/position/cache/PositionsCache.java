package chess.web.domain.position.cache;

import chess.web.dao.position.PositionRepository;
import chess.web.domain.position.Position;
import chess.web.domain.position.type.File;
import chess.web.domain.position.type.Rank;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;


public class PositionsCache {
    private static final List<Position> positions = new ArrayList<>();

    private PositionsCache() {
    }

    static {
        cachePositions();
    }

    private static void cachePositions() {
        List<Rank> ranks = Arrays.asList(Rank.values());
        List<Rank> reversedRanks = ranks.stream()
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());

        for (Rank rank : reversedRanks) {
            cachingPositionsOfRank(rank);
        }
    }

    private static void cachingPositionsOfRank(Rank rank) {
        for (File file : File.values()) {
            positions.add(new Position(file, rank));
        }
    }

    public static Position find(File file, Rank rank) {
        return positions.stream()
            .filter(position -> position.getFile() == file && position.getRank() == rank)
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 위치 입니다."));
    }

    public static Position get(int index) {
        return positions.get(index);
    }
}
