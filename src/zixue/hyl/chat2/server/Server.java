package zixue.hyl.chat2.server;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import zixue.hyl.chat2.client.ChatRoom;
import zixue.hyl.chat2.client.Client;
import zixue.hyl.chat2.util.XMLUtil;



public class Server extends WindowAdapter 
{
	
	JFrame frame;
	JPanel p1,p2,p3,p4;
	JButton button;
	JLabel label1,label2,label3;
	JTextField serverPortNum;
	Client client;
	


	Map<String,ServerMessageThread> map = new HashMap<String,ServerMessageThread>();
	
	

	public Map<String, ServerMessageThread> getMap() {
		return map;
	}

	public JTextField getServerPortNum() {
		return serverPortNum;
	}
	
	public JButton getButton() {
		return button;
	}


	public void setButton(JButton button) {
		this.button = button;
	}
	

	public JLabel getLabel2() {
		return label2;
	}

	public void setLabel2(JLabel label2) {
		this.label2 = label2;
	}


	JTextArea userList;
	
	public JTextArea getUserList() {
		return userList;
	}


	
	
	public static void main(String[] args) {
		Server serverWin = new Server();
		
		serverWin.go();
		
	}
	
	public void go(){
		frame = new JFrame("������");
		
		label1 = new JLabel("������״̬",0);
		label2 = new JLabel("ֹͣ",0);
		label2.setForeground(Color.RED);
		label3 = new JLabel("�˿ں�");
		
		button  = new JButton("����������");
		
		
		
		
		serverPortNum = new JTextField(10);
		serverPortNum.setText("5005");
		userList = new JTextArea(8,40);
		
		p1 = new JPanel();
		p2 = new JPanel();
		p3 = new JPanel();
		p4 = new JPanel();
		
		p1.add(label1);
		p1.add(label2);
		p1.setLayout(new GridLayout(1,2));
		
		p2.add(label3);
		p2.add(serverPortNum);
		p2.add(button);
		
		p3.add(p1);
		p3.add(p2);
		p3.setLayout(new GridLayout(2,1));
		Border etched = BorderFactory.createEtchedBorder();
		Border border = BorderFactory.createTitledBorder(etched,"��������Ϣ");
		p3.setBorder(border);
		
		border = BorderFactory.createTitledBorder(etched,"�����û��б�");
		p4.setBorder(border);
		p4.add(userList);
		
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				execute(e);
				
			}
			
		});
		
		//�������˴��ڹرգ�������������ͻ��˷��͹ر���Ϣ
		frame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				try{
					//���Ȼ�ȡ���ͻ��˵ļ���
					Collection<ServerMessageThread> cols = Server.this.map.values();
					//Ȼ�󹹽��������˴��ڹر���Ϣ
					String messageXML = XMLUtil.constructServerClosingXML();
					//���������̣߳����رմ��ڵ���Ϣ���ͳ�ȥ
					for(ServerMessageThread smt:cols){
						smt.sendMessage(messageXML);
					}
				}catch(Exception e1){
					e1.printStackTrace();
				}finally{
					System.exit(0);
				}
			}
			
		});
		
		
	
		Container cp = frame.getContentPane();
		cp.setLayout(new GridLayout(2,1));
		cp.add(p3);
		cp.add(p4);
		
		frame.setSize(500,400);
		frame.setVisible(true);
	}

	private void execute(ActionEvent e){
		int port = Integer.parseInt(this.serverPortNum.getText());
		new ServerConnectionThread(this,port).start();
		
	}


	
	


	
	
	
}
