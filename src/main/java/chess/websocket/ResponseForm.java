package chess.websocket;

public class ResponseForm<T> {
    public enum Form{
        UPDATE_ROOM,
        LOAD_GAME,
        NEW_USER_NAME,
        UPDATE_STATUS,
        MOVE_PIECE,
        REMOVE_ROOM;
    }

    private String form;
    private T data;

    public ResponseForm(Form form, T data) {
        this.form = form.name();
        this.data = data;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
