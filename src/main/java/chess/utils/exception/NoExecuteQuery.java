package chess.utils.exception;

public class NoExecuteQuery extends RuntimeException {

    public NoExecuteQuery(){}

    public NoExecuteQuery(String message){
        super(message);
    }
}
