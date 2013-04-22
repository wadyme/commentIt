package cn.ls.integrator.components.metadata;

import java.util.Properties;

public class DataSourceMetadata {
	
	private String dbType;

	
	private String driverClassName;
	
	private String url;
	
	private String username;
	
	private String password;
	
	private String maxActive;
	
	private String maxWait;
	
	private String poolPreparedStatements;
	
	private String defaultAutoCommit;
	
	private Properties properties;

	public Properties getProperties(){
		properties = new Properties();
		properties.put("driverClassName", driverClassName);
		properties.put("url", url);
		properties.put("username", username);
		properties.put("password", password);
		properties.put("maxActive", maxActive);
		properties.put("maxWait", maxWait);
		properties.put("poolPreparedStatements", poolPreparedStatements);
		properties.put("defaultAutoCommit", defaultAutoCommit);
		return properties;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((defaultAutoCommit == null) ? 0 : defaultAutoCommit.hashCode());
		result = prime * result + ((driverClassName == null) ? 0 : driverClassName.hashCode());
		result = prime * result + ((maxActive == null) ? 0 : maxActive.hashCode());
		result = prime * result + ((maxWait == null) ? 0 : maxWait.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((poolPreparedStatements == null) ? 0 : poolPreparedStatements.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataSourceMetadata other = (DataSourceMetadata) obj;
		if (defaultAutoCommit == null) {
			if (other.defaultAutoCommit != null)
				return false;
		} else if (!defaultAutoCommit.equals(other.defaultAutoCommit))
			return false;
		if (driverClassName == null) {
			if (other.driverClassName != null)
				return false;
		} else if (!driverClassName.equals(other.driverClassName))
			return false;
		if (maxActive == null) {
			if (other.maxActive != null)
				return false;
		} else if (!maxActive.equals(other.maxActive))
			return false;
		if (maxWait == null) {
			if (other.maxWait != null)
				return false;
		} else if (!maxWait.equals(other.maxWait))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (poolPreparedStatements == null) {
			if (other.poolPreparedStatements != null)
				return false;
		} else if (!poolPreparedStatements.equals(other.poolPreparedStatements))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getPoolPreparedStatements() {
		return poolPreparedStatements;
	}

	public void setPoolPreparedStatements(String poolPreparedStatements) {
		this.poolPreparedStatements = poolPreparedStatements;
	}

	public String getDefaultAutoCommit() {
		return defaultAutoCommit;
	}

	public void setDefaultAutoCommit(String defaultAutoCommit) {
		this.defaultAutoCommit = defaultAutoCommit;
	}

	public String getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(String maxActive) {
		this.maxActive = maxActive;
	}

	public String getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(String maxWait) {
		this.maxWait = maxWait;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
}
