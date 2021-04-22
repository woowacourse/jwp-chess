package chess.dao;

public class MysqlConnectionProperty extends DBMSProperty {
    private static final String SERVER = "jdbc:mysql://localhost:13306"; // 서버 주소
    private static final String DATABASE = "woowa_chess"; // DATABASE 이름
    private static final String OPTION = "?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8"; // DATABASE 옵션
    private static final String URL = SERVER + "/" + DATABASE + OPTION; // DATABASE 옵션
    private static final String USER = "root"; // 서버 아이디
    private static final String PASSWORD = "root"; // 서버 비밀번호

    public MysqlConnectionProperty() {
        super(URL, USER, PASSWORD);
    }
}
