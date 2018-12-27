package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import bean.Province;
import bean.Weather;
import util.ConnectDBUtil;

public class WeatherDao {
	private Statement st;
	private PreparedStatement pst;
	private ResultSet rs;
	private Connection conn;
	private ConnectDBUtil connectDBUtil;
	
	public WeatherDao(){
		this.connectDBUtil = new ConnectDBUtil();
	}
	
	public int addWeather(int idWeather, int date, float humidity, float wind, String weatherDescription, float tempAverage, float tempMin, float tempMax, String icon, int idProvince) {
		int result = 0;
		conn = connectDBUtil.getConnection();
		String sql = "INSERT INTO weather(idWeather, date, humidity, wind, weatherDescription, tempAverage, tempMin, tempMax, icon, idProvince) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, idWeather);
			pst.setInt(2, date);
			pst.setFloat(3, humidity);
			pst.setFloat(4, wind);
			pst.setString(5, weatherDescription);
			pst.setFloat(6, tempAverage);
			pst.setFloat(7, tempMin);
			pst.setFloat(8, tempMax);
			pst.setString(9, icon);
			pst.setInt(10, idProvince);
			result = pst.executeUpdate(); 
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			if(pst !=null){
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn !=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	

	
	public ArrayList<Weather> getItemsWeather(int idProvince, int timeTodayStart, int time5moredays){
		ArrayList<Weather> listItem = new ArrayList<>();
		conn = connectDBUtil.getConnection();
		String sql = "SELECT DISTINCT date, humidity,wind, weatherDescription, tempAverage, tempMin, tempMax, icon, idProvince FROM weather WHERE idProvince ="+idProvince+" AND date >="+timeTodayStart+" AND date <="+time5moredays+" ORDER BY date DESC";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			
			while(rs.next()){
				Weather objItem = new Weather(0,rs.getInt("date"), rs.getFloat("humidity"), rs.getFloat("wind"),rs.getString("weatherDescription"), rs.getFloat("tempAverage"), rs.getFloat("tempMin"), rs.getFloat("tempMax"), rs.getString("icon"), rs.getInt("idProvince") );
				listItem.add(objItem);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs !=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(st !=null){
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn !=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return listItem;
	}
	
	public static void main(String[] args) {
		WeatherDao sQLWeather = new WeatherDao();
		
	}
	
}


