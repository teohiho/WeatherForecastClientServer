package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDBUtil {
	private Connection conn;
	private String url;
	private String username;
	private String password;
	
	public ConnectDBUtil(){
		this.url="jdbc:mysql://localhost:3306/weatherclientserver?userUnicode=true&characterEncoding=UTF-8";
		this.username="root";
		this.password="";
	}
	
	public Connection getConnection(){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	public static void main(String[] args){
		ConnectDBUtil connectDB = new ConnectDBUtil();
		System.out.println("abc");
		System.out.println("hihih:"+connectDB.getConnection());
	}
}
