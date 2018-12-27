package socket;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import bean.Province;
import bean.Weather;

public class Client{
	private JFrame frame;
	private JButton showButton;
	private JButton resetButton;
	private JComboBox provinceCBB;
	private Province provinceItem;

	//-------
	private Socket socket;
	private String serverAddress = "192.168.1.73";
	private Integer serverPort = 56789;
	private JButton provinceBTN;
	private DataInputStream dis;
	private DataOutputStream dos;
	private JPanel selectPanel;
	//private String[] arrProvince = { "Ha Noi", "Sài Gòn", "Da Nang", "Hai Phong", "Quang Nam", "Đà Lạt" };
	Province pv1 = new Province(1591527, "Bắc Giang");
	Province pv2 = new Province(1591527, "Bắc Ninh");
	Province pv3 = new Province(1591538, "Bắc Kạn");
	Province pv4 = new Province(1586185, "Cao Bằng");
	Province pv5 = new Province(1581298, "Hải Phòng");
	Province pv6 = new Province(1581326, "Hải Dương");
	Province pv7 = new Province(1580142, "Hưng Yên");
	Province pv8 = new Province(1581349, "Hà Giang");
	Province pv9 = new Province(1576633, "Lạng Sơn");
	Province pv10 = new Province(1573517, "Nam Định");
	Province pv11 = new Province(1571968, "Ninh Bình");
	Province pv12 = new Province(1580142, "Phủ Lý");
	Province pv13 = new Province(1571067, "Phú Thọ");
	Province pv14 = new Province(1583992, "Đà Nẵng");
	
	
	Province[] pvs = new Province[] {pv1, pv2, pv3, pv4, pv5, pv6, pv7, pv8, pv9, pv10, pv11, pv12, pv13, pv14};
	
	
	public  Client() throws IOException {
		initGUI();
		addListeners(); 
	}
	
	private void initGUI() throws IOException{
		frame = new JFrame("Dự báo thời tiết");
		frame.setSize(550,650);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(3, 1));
		frame.add(SelectPanel());
		frame.setVisible(true);  
	}
	
	private void showWeather (ArrayList<Weather> listWeather) throws IOException {
		frame.add(weatherCur(listWeather));
	    frame.add(weathers(listWeather));
	    frame.setVisible(true);
	}
	// Panel Chọn
	private JPanel SelectPanel() throws IOException{ 
		selectPanel = new JPanel();
	    selectPanel.setLayout(new FlowLayout());
		JLabel selectLabel = new JLabel("");        
		selectLabel.setText("what do you want? "); 
		provinceCBB = new JComboBox(new DefaultComboBoxModel(pvs));
		provinceCBB.setRenderer(new DefaultListCellRenderer() {
          public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
              super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
              if(value instanceof Province){
              	Province province = (Province) value;
                  setText(province.getName());
              }
              return this;
          }
		});
	  
		JLabel label = new JLabel();
		BufferedImage image = ImageIO.read(new File("image/Weather-Forecast-Landing-page.jpg"));
        ImageIcon icon1 = new ImageIcon(image.getScaledInstance(550, 180, image.SCALE_SMOOTH));
        label.setIcon(icon1);
		
        ImageIcon icon = new ImageIcon("image/select.png");
        showButton = new JButton("Show");
        showButton.setIcon(icon);
        showButton.setBackground(new Color(255, 255, 220));
        showButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        
        resetButton = new JButton("Reset");
        
        selectPanel.add(selectLabel);
        selectPanel.add(provinceCBB);
        selectPanel.add(showButton);  
        selectPanel.add(resetButton);  
        selectPanel.add(label);   
        return selectPanel;             
   }
	
	private void addListeners() {
		showButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Hide 
//				showButton.setEnabled(false);
				provinceItem = (Province) provinceCBB.getSelectedItem();	
				//---------------------
				// #1: khởi tạo client Socket
				try {
					socket = new Socket(serverAddress, serverPort);
//					socket.setSoTimeout(1000);
				} catch(UnknownHostException e) {
					System.err.println("Không kết nối được với máy chủ: "+ e);
				} catch (IOException e) {
					System.err.println("Không thể nạp cổng: "+ serverPort);
					System.out.println(e);
				}
				
				// #2: tạo luồng để truyền tin
				try {
					dis = new DataInputStream(socket.getInputStream());
					dos = new DataOutputStream(socket.getOutputStream());
				} catch (IOException e) {
					System.err.println("Luồng vào/ra bị lỗi: " + e);
				}
				
				// #3: lấy 1 tỉnh từ người dùng, đẩy lên Server
				try {
					dos.writeInt(provinceItem.getId());
					System.out.print("\nĐã đẩy chuỗi lên Server");
				} catch (IOException e) {
					System.err.println("Luồng ra bị lỗi: " + e);
				}
				
				// #4: lấy kết quả trả về từ Server, in ra màn hình 
				try {
					ArrayList<Weather>listWeather = new ArrayList<Weather>();
					String inString = dis.readUTF();
				    String str[]=inString.split("&");
				    for(int i =0; i < str.length; i++) {
				    	String arr[] = str[i].split(",");
				    	Weather weather = new Weather(0,Integer.parseInt(arr[0]),Float.parseFloat(arr[1]),Float.parseFloat(arr[2]), arr[3], Float.parseFloat(arr[4]), Float.parseFloat(arr[5]), Float.parseFloat(arr[6]), arr[7], Integer.parseInt(arr[8]));
				    	listWeather.add(weather);
				    	System.out.println("Mảng >> : " +str[i]);
				    }
				    showWeather(listWeather);
				} catch (IOException e) {
					System.err.println("Lỗi:" + e);
				}
				
			}
		});
//		resetButton.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				ArrayList<Weather>listWeather = new ArrayList<Weather>();
//				try {
//					Weather weather = new Weather(0,0,0f,0f, "", 0f,0f,0f, "01d", 1591527);
//					listWeather.add(weather);
//					listWeather.add(weather);
//					listWeather.add(weather);
//					listWeather.add(weather);
//					listWeather.add(weather);
//					listWeather.add(weather);
//					frame.add(weatherCur(listWeather));
//				    frame.add(weathers(listWeather));
//				    frame.setVisible(true);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
	}
	
	public JPanel weatherLeft(String name, String date, String desciption, float temp, String icon) throws IOException {
		JPanel weatherLeftPanel = new JPanel();
		weatherLeftPanel.setLayout(new GridLayout(5, 1));
		weatherLeftPanel.setBackground(Color.WHITE);
		JLabel nameLabel = new JLabel();
		nameLabel.setFont(new Font("Arial",Font.CENTER_BASELINE,20));
		JLabel dateLabel = new JLabel();
		dateLabel.setFont(new Font("Arial",Font.CENTER_BASELINE,17));
		JLabel desciptionLabel = new JLabel();
		
		BufferedImage myPicture = ImageIO.read(new File("D:\\TEO TEO\\STUDY\\TH Lap trinh Mang\\Code\\DoAnMang\\"+icon+".png"));
		ImageIcon iconI = new ImageIcon(myPicture);
		Image scaleImage = iconI.getImage().getScaledInstance(100, 100,Image.SCALE_DEFAULT);
		JLabel iconLabel = new JLabel(new ImageIcon(scaleImage));
		iconLabel.setFont(new Font("",Font.CENTER_BASELINE,100));
		
		JLabel tempCLabel = new JLabel();
		tempCLabel.setFont(new Font("Arial",Font.PLAIN,30));
		nameLabel.setText(name + ", Việt Nam");
		dateLabel.setText(date);
		desciptionLabel.setText(desciption);
		tempCLabel.setText(Float.toString(temp) + "⁰C");
		weatherLeftPanel.add(nameLabel);
		weatherLeftPanel.add(dateLabel);
		weatherLeftPanel.add(desciptionLabel);
		weatherLeftPanel.add(iconLabel);
		weatherLeftPanel.add(tempCLabel);
		return weatherLeftPanel;
	}
	
	public JPanel weatherRight(int humidity, int wind) {
		
		JPanel weatherRightPanel = new JPanel(new BorderLayout(10, 10));
		weatherRightPanel.setBackground(Color.WHITE);
		weatherRightPanel.add(slider("Humidity", 0, 100, humidity, "%", 20), BorderLayout.WEST);
		weatherRightPanel.add(slider("Wind", 0, 5, wind, " mps", 1), BorderLayout.EAST);
		
		return weatherRightPanel;	
	}
	
	public JPanel slider(String name, int min, int max, int value, String symbol, int muc) {
		JPanel sliderPanel = new JPanel();
		sliderPanel.setLayout(new GridLayout(3, 1));
		sliderPanel.setBackground(Color.WHITE);
		JLabel valueText = new JLabel();
		valueText.setText(Integer.toString(value)+ symbol);
		JSlider slider = new JSlider(JSlider.VERTICAL, min, max, 0);
		slider.setEnabled(false);
		slider.setPaintTicks(true);
		slider.setMajorTickSpacing(muc);
		slider.setMinorTickSpacing(10);
		slider.setPaintLabels(true);
		slider.setValue(value);
		JLabel sliderText = new JLabel();
		sliderText.setText(name);
		sliderPanel.add(valueText);
		sliderPanel.add(slider);
		sliderPanel.add(sliderText);
		return sliderPanel;
	}
	//----------- Panel thời tiết hôm nay
	public JPanel weatherCur(ArrayList<Weather> listWeather) throws IOException {
		JPanel weatherCurPanel = new JPanel(new BorderLayout(10, 10));
		weatherCurPanel.setBackground(Color.WHITE);
		weatherCurPanel.add(weatherLeft(provinceItem.getName(), 
										convertUnixTimeStamptoDate(listWeather.get(4).getDate()),
										listWeather.get(4).getWeatherDescription(), 
										listWeather.get(4).getTempMax(), 
										listWeather.get(4).getIcon()), BorderLayout.WEST);
		weatherCurPanel.add(weatherRight((int)listWeather.get(4).getHumidity(), 
										(int)listWeather.get(4).getWind()), BorderLayout.EAST);
		
		return weatherCurPanel;
	}
	
	public JPanel aWeather(String date, int tempMax, int tempMin, String image) throws IOException {
		JPanel weather4daysPanel = new JPanel();
		weather4daysPanel.setLayout(new GridLayout(7, 1));
		Border border = BorderFactory.createTitledBorder(date);
		weather4daysPanel.setBorder(border);
		
		BufferedImage myPicture = ImageIO.read(new File("D:\\TEO TEO\\STUDY\\TH Lap trinh Mang\\Code\\DoAnMang\\"+image+".png"));
		ImageIcon icon = new ImageIcon(myPicture);
		Image scaleImage = icon.getImage().getScaledInstance(50, 50,Image.SCALE_SMOOTH);
		JLabel iconLabel = new JLabel(new ImageIcon(scaleImage));
		iconLabel.setFont(new Font("",Font.BOLD,100));
		
		JLabel tempMaxLabel = new JLabel();
		tempMaxLabel.setFont(new Font("Arial",Font.PLAIN,20));
		
		JLabel tempMinLabel = new JLabel();
		
		tempMaxLabel.setText(Integer.toString(tempMax) + "⁰C");
		tempMinLabel.setText(Integer.toString(tempMin) + "⁰C");
	
		weather4daysPanel.add(iconLabel);
		weather4daysPanel.add(tempMaxLabel);
		weather4daysPanel.add(tempMinLabel);
		return weather4daysPanel;
	}
	
	//----------- Dự đoán thời tiết các ngày khác
	public JPanel weathers(ArrayList<Weather> listWeather) throws IOException {
		JPanel weathersPanel = new JPanel();
		weathersPanel.setLayout(new GridLayout(1, 4));
		weathersPanel.add(aWeather(convertUnixTimeStamptoDate(listWeather.get(3).getDate()), 
								(int)listWeather.get(3).getTempMax(), 
								(int)listWeather.get(3).getTempMin(), 
								listWeather.get(3).getIcon()));
		weathersPanel.add(aWeather(convertUnixTimeStamptoDate(listWeather.get(2).getDate()), 
								(int)listWeather.get(2).getTempMax(), 
								(int)listWeather.get(2).getTempMin(), 
								listWeather.get(2).getIcon()));
		weathersPanel.add(aWeather(convertUnixTimeStamptoDate(listWeather.get(1).getDate()), 
								(int)listWeather.get(1).getTempMax(), 
								(int)listWeather.get(1).getTempMin(), 
								listWeather.get(1).getIcon()));
		weathersPanel.add(aWeather(convertUnixTimeStamptoDate(listWeather.get(0).getDate()), 
								(int)listWeather.get(0).getTempMax(), 
								(int)listWeather.get(0).getTempMin(), 
								listWeather.get(0).getIcon()));
		return weathersPanel;
	}
	
	//----------- Giây -> Ngày
	public String convertUnixTimeStamptoDate(int dt) {
        Date dateTime = new Date(dt*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT-7")); 
        String formattedDate = sdf.format(dateTime);
        return formattedDate;
	}
	
	public static void main(String[] args) throws IOException {
		Client test = new Client();
	} 
	
}