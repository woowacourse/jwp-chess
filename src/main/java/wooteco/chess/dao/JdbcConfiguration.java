package wooteco.chess.dao;

public class JdbcConfiguration {
	private String url;
	private String option;
	private String username;
	private String password;
	private String driverClassName;

	public JdbcConfiguration() {
	}

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

	public String getUrl() {
		return url;
	}

	public String getOption() {
		return option;
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

		public void setUrl(String url) {
			this.url = url;
		}

		public void setOption(String option) {
			this.option = option;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public void setDriverClassName(String driverClassName) {
			this.driverClassName = driverClassName;
		}

		public Builder url(String url) {
			this.url = url;
			return this;
		}

		public Builder option(String option) {
			this.option = option;
			return this;
		}

		public Builder username(String username) {
			this.username = username;
			return this;
		}

		public Builder password(String password) {
			this.password = password;
			return this;
		}

		public Builder driverClassName(String driverClassName) {
			this.driverClassName = driverClassName;
			return this;
		}

		public JdbcConfiguration build() {
			return new JdbcConfiguration(url, option, username, password, driverClassName);
		}
	}
}