import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;



public class testGUI extends JFrame implements ActionListener{
	JTextArea allMessagesTa;
    JPanel titlePanel, messagePanel, inputPanel;
    JLabel titleLb;
    JButton resetBtn;
    JButton cancelBtn;
    String address = "localhost";
    int port = 7000;
	public testGUI() {
		super();
		initGUI();
	}
	public void initGUI() {
		 this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	      this.setLayout(new BorderLayout());
	      titlePanel = new JPanel();
	      titleLb = new JLabel(address + ":" + port + " >> Message Server");
	      titlePanel.add(titleLb);
	      messagePanel = new JPanel();
	      messagePanel.setLayout(new BorderLayout());
	      allMessagesTa = new JTextArea();
	      allMessagesTa.setWrapStyleWord(true);
	      allMessagesTa.setLineWrap(true);
	      allMessagesTa.setEditable(false);
	      JScrollPane scrollPane = new JScrollPane(allMessagesTa);
	      scrollPane.setVerticalScrollBarPolicy(
	 					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	      messagePanel.add(scrollPane);
	      inputPanel = new JPanel();
	      inputPanel.setLayout(new BorderLayout());
	      cancelBtn = new JButton("Cancel");
	      resetBtn = new JButton("Reset");
	      cancelBtn.addActionListener(this);
	      resetBtn.addActionListener(this);
	      
	      Font font = new Font("Consolas", Font.PLAIN, 15);
	      allMessagesTa.setForeground(Color.WHITE);
	      allMessagesTa.setBackground(Color.BLACK);
	      allMessagesTa.setFont(font);
	      allMessagesTa.append(currentTimeStamps()+">> "+"Server đã khởi dộng!" + "\n");
	      allMessagesTa.append(currentTimeStamps()+">> "+"Đang lấy API về" + "\n");
	      inputPanel.add(resetBtn, BorderLayout.PAGE_START);
	      inputPanel.add(cancelBtn, BorderLayout.PAGE_END);
	      add(titlePanel, BorderLayout.NORTH);
	      add(messagePanel,BorderLayout.CENTER);
	      add(inputPanel, BorderLayout.SOUTH);
	      setSize(700, 400);
	      setLocation(400, 100);
	      setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String button = e.getActionCommand();
		    if (button.equals("Reset")) {
		    	allMessagesTa.setText("");
		    }
		    if (button.equals("Cancel")) {
		      System.exit(0);
		    }
		
	}
	//----------- Ngày hiện tại
	public String currentTimeStamps() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		String formattedDate = sdf.format(currentTime);
		return formattedDate;
	}
	public static void main(String[] args) {
		new testGUI();
	}
}
