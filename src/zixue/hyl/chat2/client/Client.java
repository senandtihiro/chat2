package zixue.hyl.chat2.client;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;


public class Client
{
	JFrame frame;
	JPanel p1,p2,p3,p4,p5;
	JLabel label1,label2,label3;
	JButton button1,button2;
	public JTextField getUsername() {
		return username;
	}

	public void setUsername(JTextField username) {
		this.username = username;
	}

	public JTextField getServerAddr() {
		return serverAddr;
	}

	public void setServerAddr(JTextField serverAddr) {
		this.serverAddr = serverAddr;
	}

	public JTextField getPort() {
		return port;
	}

	public void setPort(JTextField port) {
		this.port = port;
	}

	JTextField username,serverAddr,port;
	
	static List<String> userList;
	
	
	public static void main(String[] args) {
		Client clientWin = new Client();
		clientWin.go();
	}
	
	public void go(){
		frame = new JFrame("用户登录");
		
		
		
		p1 = new JPanel();
		label1 = new JLabel("用户名",SwingConstants.LEFT);
		username = new JTextField(15);
		p1.add(label1);
		p1.add(username);
		
		p2 = new JPanel();
		label2 = new JLabel("服务器地址",SwingConstants.LEFT);
		serverAddr = new JTextField(15);
		p2.add(label2);
		p2.add(serverAddr);
		
		p3 = new JPanel();
		label3 = new JLabel("端口号",SwingConstants.LEFT);
		port = new JTextField(15);
		p3.add(label3);
		p3.add(port);
		
		p4 = new JPanel();
		button1 = new JButton("登录");
		button2 = new JButton("重置");
		p4.add(button1);
		p4.add(button2);
		
		p5 = new JPanel();
		p5.add(p1);
		p5.add(p2);
		p5.add(p3);
		p5.add(p4);
		
		Border etched = BorderFactory.createEtchedBorder();
		Border border = BorderFactory.createTitledBorder(etched,"用户登录");
		p5.setBorder(border);
		p5.setLayout(new GridLayout(4,1));
		
		button1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				login(e);
			}
			
		});
				
		
		
		button2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				username.setText(null);
				serverAddr.setText(null);
				port.setText(null);
			}
			
		});
		
		
		
		
		
		Container cp = frame.getContentPane();
		cp.add(p5);
		
		username.setText("zhangsan");
		serverAddr.setText("localhost");
		port.setText("5005");
		
		frame.setSize(400, 400);
		frame.setVisible(true);

		
	}
	
	private void login(ActionEvent e){
		String username = Client.this.username.getText();
		String hostAddress = Client.this.serverAddr.getText();
		int port = Integer.parseInt(Client.this.port.getText());
		
//		new ClientConnectionThread(this,username,hostAddress,port).start();
		ClientConnectionThread clientConnectionThread = new ClientConnectionThread(this,username,hostAddress,port);
		if(clientConnectionThread.login()){
			clientConnectionThread.start();
		}
		else{
			JOptionPane.showMessageDialog(frame, "用户名重复","信息",JOptionPane.WARNING_MESSAGE);
		}
	}

}
