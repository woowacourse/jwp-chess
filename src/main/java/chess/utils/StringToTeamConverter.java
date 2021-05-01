package chess.utils;

import chess.domain.game.team.Team;
import org.springframework.core.convert.converter.Converter;

public class StringToTeamConverter implements Converter<String, Team> {

    @Override
    public Team convert(final String source) {
        return Team.from(source);
    }

}
