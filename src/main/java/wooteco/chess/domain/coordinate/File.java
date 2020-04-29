package wooteco.chess.domain.coordinate;

import java.util.HashMap;
import java.util.Map;

public enum File {
    A("a", 1),
    B("b", 2),
    C("c", 3),
    D("d", 4),
    E("e", 5),
    F("f", 6),
    G("g", 7),
    H("h", 8);

    private static final Map<String, File> bySymbol = new HashMap<>();
    private static final Map<Integer, File> byValue = new HashMap<>();

    static {
        for (File file: values()) {
            bySymbol.put(file.symbol, file);
            byValue.put(file.value, file);
        }
    }

    private final String symbol;
    private final int value;

    File(final String symbol, final int value) {
        this.symbol = symbol;
        this.value = value;
    }

    public static File findByValue(int value) {
        if(!byValue.containsKey(value)) {
            throw new IllegalArgumentException("file : "+value+", file의 value는 1부터 8까지 입니다.");
        }
        return byValue.get(value);
    }

    public static File findBySymbol(String symbol) {
        if(!bySymbol.containsKey(symbol)) {
            throw new IllegalArgumentException("file : "+symbol+", file의 value는 1부터 8까지 입니다.");
        }
        return bySymbol.get(symbol);
    }

    public int subtract(File file) {
        return this.value - file.value;
    }

    public File sum(int fileValue) {
        return findByValue(this.value + fileValue);
    }

    public String getSymbol() {
        return symbol;
    }
}
