package cn.ls.integrator.components;

import cn.ls.integrator.core.manager.ConnectionManager;
import cn.ls.integrator.core.manager.impl.ConnectionManagerImpl;
import cn.ls.integrator.core.model.Connection;

public class ConnectionTest {

	public static void main(String[] args) {
		ConnectionManager connManger = ConnectionManagerImpl.getInstance();
		Connection connection = new Connection();
//		connection.setDbtype("db2");
//		connection.setUrl("jdbc:db2://192.168.1.103:50000/EPMONDB");
//		connection.setDriver("com.ibm.db2.jcc.DB2Driver");
//		connection.setUsername("EPMONDB");
//		connection.setPassword("Windows2008@");
		connection.setDbtype("oracle");
		connection.setUrl("jdbc:oracle:thin:@192.168.1.143:1521:orcl");
		connection.setDriver("oracle.jdbc.driver.OracleDriver");
		connection.setUsername("sys");
		connection.setPassword("sys");
		System.out.println(connManger.testConnect(connection));
	}
}
