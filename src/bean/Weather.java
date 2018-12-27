package bean;

public class Weather {
	private int idWeather;
	private int date;
	private float humidity; 
	private float wind;
	private String weatherDescription; 
	private float tempAverage;
	private float tempMin;
	private float tempMax; 
	private String icon;
	private int idProvince;
	
	public Weather(int idWeather, int date, float humidity, float wind, String weatherDescription, float tempAverage,
			float tempMin, float tempMax, String icon, int idProvince) {
		super();
		this.idWeather = idWeather;
		this.date = date;
		this.humidity = humidity;
		this.wind = wind;
		this.weatherDescription = weatherDescription;
		this.tempAverage = tempAverage;
		this.tempMin = tempMin;
		this.tempMax = tempMax;
		this.icon = icon;
		this.idProvince = idProvince;
	}
	public int getIdWeather() {
		return idWeather;
	}
	public void setIdWeather(int idWeather) {
		this.idWeather = idWeather;
	}
	public int getDate() {
		return date;
	}
	public void setDate(int date) {
		this.date = date;
	}
	public float getHumidity() {
		return humidity;
	}
	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}
	public float getWind() {
		return wind;
	}
	public void setWind(float wind) {
		this.wind = wind;
	}
	public String getWeatherDescription() {
		return weatherDescription;
	}
	public void setWeatherDescription(String weatherDescription) {
		this.weatherDescription = weatherDescription;
	}
	public float getTempAverage() {
		return tempAverage;
	}
	public void setTempAverage(float tempAverage) {
		this.tempAverage = tempAverage;
	}
	public float getTempMin() {
		return tempMin;
	}
	public void setTempMin(float tempMin) {
		this.tempMin = tempMin;
	}
	public float getTempMax() {
		return tempMax;
	}
	public void setTempMax(float tempMax) {
		this.tempMax = tempMax;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getIdProvince() {
		return idProvince;
	}
	public void setIdProvince(int idProvince) {
		this.idProvince = idProvince;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getDate() + "," + getHumidity() + "," + getWind() + "," + getWeatherDescription() + "," + getTempAverage() + "," + getTempMin() + "," + getTempMax() + "," + getIcon() + "," + getIdProvince();
	}
	

	
	
	
}
