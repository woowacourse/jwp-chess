package chess.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class ParameterSourceCreator {

    private ParameterSourceCreator() {
    }

    public static SqlParameterSource makeParameterSource(List<String> keys, List<Object> values) {
        Map<String, Object> parameters = new HashMap<>();

        for (int i = 0; i < keys.size(); i++) {
            parameters.put(keys.get(i), values.get(i));
        }
        return new MapSqlParameterSource(parameters);
    }
}
