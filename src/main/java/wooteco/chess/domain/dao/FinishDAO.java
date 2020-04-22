package wooteco.chess.domain.dao;

import java.sql.*;

public class FinishDAO {
    private Connection connection;

    public FinishDAO() {
        this.connection = getConnection();
    }

    private Connection getConnection() {
        Connection con = null;
        String server = "localhost:13306"; // MySQL 서버 주소
        String database = "chess"; // MySQL DATABASE 이름
        String option = "?useSSL=false&serverTimezone=UTC";
        String userName = "root"; //  MySQL 서버 아이디
        String password = "root"; // MySQL 서버 비밀번호

        // 드라이버 로딩
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 드라이버 연결
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + server + "/" + database + option, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    private void closeConnection() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException ignored) {
        }
    }

    public void insertFinish(final boolean isFinish) throws SQLException {
        String query = "INSERT INTO finish (isFinish) VALUES (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, String.valueOf(isFinish));
        preparedStatement.executeUpdate();
    }

    public void updateFinish(final boolean isFinish) throws SQLException {
        String query = "UPDATE finish set isFinish = (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, String.valueOf(isFinish));
        preparedStatement.executeUpdate();
    }

    public String getIsFinish() throws SQLException {
        String query = "SELECT * FROM finish";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        String output = "";
        while(resultSet.next()) {
            output = resultSet.getString("isFinish");
        }
        return output;
    }

    public void deleteFinish() throws SQLException {
        String query = "TRUNCATE finish";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.executeUpdate();
    }
}
