package wooteco.chess.dao;

public class JdbcConfiguration {
	private static final String URL = "jdbc:%s://%s/%s%s";

	private final String server;
	private final String database;
	private final String option;
	private final String userName;
	private final String password;
	private final String databaseDriver;
	private final String dataSourceName;

	private JdbcConfiguration(String server, String database, String option, String userName, String password,
			String databaseDriver, String dataSourceName) {
		this.server = server;
		this.database = database;
		this.option = option;
		this.userName = userName;
		this.password = password;
		this.databaseDriver = databaseDriver;
		this.dataSourceName = dataSourceName;
	}

	public String getDatabaseDriver() {
		return databaseDriver;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getUrl() {
		return String.format(URL, dataSourceName, server, database, option);
	}

	public static class Builder {
		private String server;
		private String database;
		private String option;
		private String userName;
		private String password;
		private String databaseDriver;
		private String dataSourceName;

		public Builder server(String server) {
			this.server = server;
			return this;
		}

		public Builder database(String database) {
			this.database = database;
			return this;
		}

		public Builder option(String option) {
			this.option = option;
			return this;
		}

		public Builder userName(String userName) {
			this.userName = userName;
			return this;
		}

		public Builder password(String password) {
			this.password = password;
			return this;
		}

		public Builder databaseDriver(String databaseDriver) {
			this.databaseDriver = databaseDriver;
			return this;
		}

		public Builder dataSourceName(String dataSourceName) {
			this.dataSourceName = dataSourceName;
			return this;
		}

		public JdbcConfiguration build() {
			return new JdbcConfiguration(server, database, option, userName, password, databaseDriver, dataSourceName);
		}
	}
}