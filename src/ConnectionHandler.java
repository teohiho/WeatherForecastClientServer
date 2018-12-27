import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import bean.Weather;
import dao.WeatherDao;
 
public class ConnectionHandler implements Runnable{
    private Socket socket;
    private Integer serverPort = 5678;
	private ServerSocket server;
	private DataInputStream dis;
	private DataOutputStream dos;
 
   public ConnectionHandler(Socket socket) {
        this.socket = socket;
 
        Thread t = new Thread(this);
        t.start();
    }
 
    public void run() {
    	int input;
		while(true) {
			try {
			
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
				input = dis.readInt();
				
				System.out.println("Chuỗi của client: "+ input);
				int timeTodayStart = convertDatetotUnixTimeStamp(currentTimeStamps())-3600-3600-3600;
				int time5moredays = timeTodayStart + 86400*5;
				System.out.println("timeTodayStart:"+ timeTodayStart);
				System.out.println("time5moredays:"+ time5moredays);
				ArrayList<Weather> listWeather = selectSQLWeather(input, timeTodayStart, time5moredays);
				StringBuffer output = new StringBuffer();
				if(listWeather.size() == 6) {
					System.out.println("Dữ liệu đã có trong database.");
				}else {
					System.out.println("Dữ liệu chưa được cập nhập");
					getAPI(input);
					System.out.println("Đã cập nhập lại database");
				}
				//convertUnixTimeStamptoDate(1541304000);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
  //----------- Update SQL
  	public void updateSQLWeather(int idWeather, int date, float humidity, float wind, String weatherDescription, float tempAverage, float tempMin, float tempMax, String icon, int idProvince) {
  		WeatherDao sqlWeather = new WeatherDao();
  		if(sqlWeather.addWeather(idWeather, date, humidity, wind, weatherDescription, tempAverage, tempMin, tempMax, icon, idProvince) > 0) {
  			
  		}
  	}
  	
  	//----------- Select SQL
  	public ArrayList<Weather> selectSQLWeather(int ID_PROVINCE, int timeTodayStart, int time5moredays) {
  		WeatherDao sqlWeather = new WeatherDao();
  		return sqlWeather.getItemsWeather(ID_PROVINCE, timeTodayStart, time5moredays);
  	}
  	
  	//----------- Giây -> Ngày
  	public String convertUnixTimeStamptoDate(int dt) {
  		// chuyển đổi giây thành mili giây
          Date dateTime = new Date(dt*1000L);
          // định dạng thời gian
          SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
          // cung cấp múi giờ
  		sdf.setTimeZone(TimeZone.getTimeZone("GMT-7")); 
          String formattedDate = sdf.format(dateTime);
          
          System.out.println("Time muốn: " + formattedDate);
          return formattedDate;
  	}
  	
  	//----------- Ngày -> Giây
  	public int convertDatetotUnixTimeStamp(String date) {
  		long unixTime = 0;
  		// định dạng thời gian
  		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
          // cung cấp múi giờ
  		sdf.setTimeZone(TimeZone.getTimeZone("GMT-7"));
  		try {
  			unixTime =  sdf.parse(date).getTime();
  			unixTime =  (unixTime / 1000);
  		} catch (ParseException e) {
  			e.printStackTrace();
  		}
  		return (int)unixTime;
  		
  	}
  	
  	//----------- Ngày hiện tại
  	public String currentTimeStamps() {
  		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
  		String formattedDate = sdf.format(currentTime);
  		return formattedDate;
  	}
  	
  	//----------- K -> C
  	public Float convertKtoC(float doK) {
  		int doC = 0;
  		doC = (int) (doK - 273.1f);
  		return (float) doC;
  	}

  	
  	public void getAPI(int ID_PROVINCE) {
  		String API_KEY = "be8d3e323de722ff78208a7dbb2dcd6f";
  		String urlString = "http://api.openweathermap.org/data/2.5/forecast/daily?id="+ ID_PROVINCE +"&cnt=6&lang=vn&appid=" + API_KEY;
  		
  		try {
  			StringBuilder result = new StringBuilder();
  			URL url;
  			url = new URL(urlString);
  			URLConnection conn = url.openConnection();
  			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
  			String line;
  			while((line = rd.readLine()) != null) {
  				result.append(line);
  			}
  			rd.close();
  			

  			JsonElement root = new JsonParser().parse(result.toString());
  		

  			for(int i = 0; i < 6; i++) {
  				String date = root.getAsJsonObject().get("list")
  						.getAsJsonArray().get(i)
  						.getAsJsonObject().get("dt").toString();
  				String humidity = root.getAsJsonObject().get("list")
  						.getAsJsonArray().get(i)
  						.getAsJsonObject().get("humidity").toString();
  				String wind = root.getAsJsonObject().get("list")
  						.getAsJsonArray().get(i)
  						.getAsJsonObject().get("speed").toString();
  				String weatherDescription = root.getAsJsonObject().get("list")
  						.getAsJsonArray().get(i)
  						.getAsJsonObject().get("weather")
  						.getAsJsonArray().get(0)
  						.getAsJsonObject().get("description")
  						.toString();
  				String tempAverage = root.getAsJsonObject().get("list")
  						.getAsJsonArray().get(i)
  						.getAsJsonObject().get("temp")
  						.getAsJsonObject().get("day").toString();
  				String tempMin = root.getAsJsonObject().get("list")
  						.getAsJsonArray().get(i)
  						.getAsJsonObject().get("temp")
  						.getAsJsonObject().get("min").toString();
  				String tempMax = root.getAsJsonObject().get("list")
  						.getAsJsonArray().get(i)
  						.getAsJsonObject().get("temp")
  						.getAsJsonObject().get("max").toString();
  				String icon = root.getAsJsonObject().get("list")
  						.getAsJsonArray().get(i)
  						.getAsJsonObject().get("weather")
  						.getAsJsonArray().get(0)
  						.getAsJsonObject().get("icon")
  						.toString();
  				System.out.println("abc>>>>>>>");
  				updateSQLWeather(0, Integer.parseInt(date), Float.parseFloat(humidity), Float.parseFloat(wind), weatherDescription.replace("\"", ""), Float.parseFloat(tempAverage), Float.parseFloat(tempMin), Float.parseFloat(tempMax), icon.replace("\"", ""), ID_PROVINCE);
  			}
  		} catch (MalformedURLException e) {
  			e.printStackTrace();
  		} catch (IOException e) {
  			e.printStackTrace();
  		}
  		
  	}
}