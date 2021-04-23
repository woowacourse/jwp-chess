package chess.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JSonHandler {
    private final ObjectMapper mapper;

    public JSonHandler(final ObjectMapper mapper){
        this.mapper = mapper;
    }

    public String mapToJsonData(final Map<String, String> map) {
        try {
            return mapper.writeValueAsString(map);
        } catch (Exception e) {
            throw new IllegalArgumentException("JSon mapping 실패 \n");
        }
    }

    public Map<String, String> jsonDataToStringMap(final String data) {
        try {
            return mapper.readValue(data, new TypeReference<Map<String, String>>() {});
        } catch (Exception e) {
            throw new IllegalArgumentException("JSon mapping 실패 \n" + data);
        }
    }
}
