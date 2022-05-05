package chess.domain.game;

//TODO: 기존에 사용하던 disabled를 반환하는 것이 아니라 html 옵션에 기본으로 Disabled를 넣고 True, false를 반환
public enum State {
    RUNNING("disabled"),
    READY(""),
    FINISHED("");

    private final String disableOption;

    State(String disableOption) {
        this.disableOption = disableOption;
    }

    public String getDisableOption() {
        return disableOption;
    }
}
