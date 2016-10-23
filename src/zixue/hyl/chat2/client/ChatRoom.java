package zixue.hyl.chat2.client;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import zixue.hyl.chat2.server.Server;

public class ChatRoom
{
	JPanel p1,p2,p3,p4,p5;
	JFrame frame;
	JTextArea onlineUserList;
	ClientConnectionThread clientConnectionThread;
	Server server;
	String toUser;
	
	
	
	
	public String getToUser() {
		return toUser;
	}



	public ChatRoom(ClientConnectionThread clientConnectionThread){
		this.clientConnectionThread = clientConnectionThread;
	}
	
	
	
	public  JTextArea getOnlineUserList() {
		return onlineUserList;
	}

	JTextArea chatRoomBox;
	public JTextArea getChatRoomBox() {
		return chatRoomBox;
	}

	JTextField message1;
	JButton button1,button2;
	
	JComboBox jcb;
	
	DefaultComboBoxModel<String> model;
	
	
	
	public DefaultComboBoxModel<String> getModel() {
		return model;
	}
	
	
	public void showUserList(List<String> userList){
		Set<String> usernames = new HashSet<String>(userList);
		this.getModel().removeAllElements();
		for(String username:usernames){
			this.getModel().addElement(username);
		}
	}


	public void go(){
		frame = new JFrame("聊天室");
		
		p1 = new JPanel();
		chatRoomBox = new JTextArea(40,30);
		p1.add(chatRoomBox);
		
		p2 = new JPanel();
		message1 = new JTextField(12);
		button1 = new JButton("发送");
		button2 = new JButton("清屏");
		
		p2.add(message1);
		p2.add(button1);
		p2.add(button2);
		
		p3 = new JPanel();
		p3.add(p1);
		p3.add(p2);
		p3.setLayout(new GridLayout(2,1));
		Border etched = BorderFactory.createEtchedBorder();
		Border border = BorderFactory.createTitledBorder(etched,"聊天室信息");
		p3.setBorder(border);
		
		p4 = new JPanel();
		onlineUserList = new JTextArea(15,20);
		p4.add(onlineUserList);
		border = BorderFactory.createTitledBorder(etched,"在线用户列表");
		p4.setBorder(border);
		
		model = new DefaultComboBoxModel<String>();
		jcb = new JComboBox<String>(model);
		p2.add(jcb);
		
		jcb.addItemListener(new ItemListener()
		{
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				toUser = comboBoxStateChanged(e);
			}
		});
			
	
		
		
		p5 = new JPanel();
		p5.add(p3);
		p5.add(p4);
		p5.setLayout(new GridLayout(1, 2));
		
		button1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try{
					ChatRoom.this.sendMessage(e);
				}
				catch(Exception ex){
					ex.printStackTrace();
				}
				
				
			}
			
		});
		
		
		
		button2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				chatRoomBox.setText(null);
			}
		});
		
		//客户端窗口关闭
		frame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				ChatRoom.this.clientConnectionThread.sendMessage("client window closed", "5");
			}
		});
		
		
		
		
		
		Container cp = frame.getContentPane();
		cp.add(p5);
		frame.setSize(600,500);
		frame.setVisible(true);
		
	}
	
	public JComboBox getJcb() {
		return jcb;
	}

	private void sendMessage(ActionEvent e){
		String msg = this.message1.getText();
		this.message1.setText("");
		this.clientConnectionThread.sendMessage(msg,"2");
	}
	
	
	private String comboBoxStateChanged(ItemEvent evt){
		if(ItemEvent.SELECTED == evt.getStateChange()){
			String toUser = jcb.getSelectedItem().toString();
			return toUser;
		}
		return null;
	}
	
}
