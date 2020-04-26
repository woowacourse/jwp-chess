package wooteco.chess.dao;

public class JdbcConfiguration {
	private String url;
	private String option;
	private String username;
	private String password;
	private String driverClassName;

	private JdbcConfiguration(String url, String option, String username, String password, String driverClassName) {
		this.url = url;
		this.option = option;
		this.username = username;
		this.password = password;
		this.driverClassName = driverClassName;
	}

	public String getFullUrl() {
		return url + option;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	@Override
	public String toString() {
		return "JdbcConfiguration{" +
				"url='" + url + '\'' +
				", option='" + option + '\'' +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", driverClassName='" + driverClassName + '\'' +
				'}';
	}

	public static class Builder {
		private String url;
		private String option;
		private String username;
		private String password;
		private String driverClassName;

		public Builder setUrl(String url) {
			this.url = url;
			return this;
		}

		public Builder setOption(String option) {
			this.option = option;
			return this;
		}

		public Builder setUsername(String username) {
			this.username = username;
			return this;
		}

		public Builder setPassword(String password) {
			this.password = password;
			return this;
		}

		public Builder setDriverClassName(String driverClassName) {
			this.driverClassName = driverClassName;
			return this;
		}

		public JdbcConfiguration build() {
			return new JdbcConfiguration(url, option, username, password, driverClassName);
		}
	}
}