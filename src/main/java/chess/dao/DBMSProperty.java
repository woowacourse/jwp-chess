package chess.dao;

public abstract class DBMSProperty implements ConnectionProperty {
    private final String url;
    private final String user; // 서버 아이디
    private final String password; // 서버 비밀번호

    protected DBMSProperty(final String url, final String user, final String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
