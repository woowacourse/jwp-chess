package wooteco.chess.dao;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mysql.datasource")
public class JdbcConfiguration {
	private String url;
	private String option;
	private String username;
	private String password;
	private String driverClassName;

	public JdbcConfiguration() {
	}

	public JdbcConfiguration(String url, String option, String username, String password, String driverClassName) {
		this.url = url;
		this.option = option;
		this.username = username;
		this.password = password;
		this.driverClassName = driverClassName;
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

	public String getUrl() {
		return url + option;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public static class Builder {
		private String url;
		private String option;
		private String username;
		private String password;
		private String driverClassName;

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