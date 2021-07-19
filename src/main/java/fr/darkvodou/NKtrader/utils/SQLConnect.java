package fr.darkvodou.NKtrader.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class SQLConnect 
{
	private static HikariConfig jdbcConfig = new HikariConfig();
	private static HikariDataSource ds = null;

	public static HikariDataSource getHikariDS() 
	{
		if(ds.isClosed())
		{
			ds = new HikariDataSource(jdbcConfig);
		}
		return ds;
	}
	
	public static void setInfo(String host_, int port_, String dbName_, String user_, String password_)
	{
		jdbcConfig.setPoolName("DVessentials");
		jdbcConfig.setMaximumPoolSize(10);
		jdbcConfig.setMinimumIdle(2);
		jdbcConfig.setMaxLifetime(900000);
		jdbcConfig.setJdbcUrl("jdbc:mysql://" + host_ + ":" + port_ + "/" + dbName_ + "?useSSL=false&autoReconnect=true&useUnicode=yes");
		jdbcConfig.setUsername(user_);
		jdbcConfig.setPassword(password_);
		ds = new HikariDataSource(jdbcConfig);
	}
}