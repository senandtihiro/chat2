package zixue.hyl.chat2.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import zixue.hyl.chat2.util.XMLUtil;

public class ClientConnectionThread extends Thread
{
	private Client client;
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private String username;
	public Socket getSocket() {
		return socket;
	}

	private String hostAddress;
	private ChatRoom chatRoom;
	
	private int port;
	public ClientConnectionThread(Client client,String username,String hostAddress,int port){
		this.client = client;
		this.username = username;
		this.hostAddress = hostAddress;
		this.port = port;
		this.connect2Server();
	}
	
	//���ӷ�����
	private void connect2Server(){
		try
		{
			socket = new Socket(this.hostAddress,this.port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	//�����¼
	private boolean isLogin = false; 
	
	public boolean login(){
		try
		{
			String loginXML = XMLUtil.constructLoginXML(this.username);
			os.write(loginXML.getBytes());//�ͻ�����������˷����û���¼��Ϣ
			
			byte[] buf = new byte[5000];
			int length = is.read(buf);
			
			String loginResultXML = new String(buf,0,length);
			String loginResult = XMLUtil.extractLoginResult(loginResultXML);
			
			if("success".equals(loginResult)){
				this.chatRoom = new ChatRoom(this);
				chatRoom.go();
				this.client.frame.setVisible(false);
				return true;
			}
			else{
				return false;
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	
	public void sendMessage(String messageXML,String type){
		String messageSend = null;
		String sendTo = this.chatRoom.getToUser();
//		System.out.println(sendTo);
		if(type == "2"){
			messageSend = XMLUtil.constructClientMessage(messageXML, this.username,sendTo);
		}else if(type == "5"){
			messageSend = XMLUtil.constructClientClosingXML(this.username);
		}
		try
		{
			this.os.write(messageSend.getBytes());
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(true){
			try
			{
				byte[] buf = new byte[5000];
				int length = is.read(buf);
				
				String xml = new String(buf,0,length);
				int type = Integer.parseInt(XMLUtil.extractType(xml));
				if(type == 4){
					List<String> userList = XMLUtil.extractUserList(xml);
					String users = "";
					for(String user:userList){
						users += user+"\n";
					}
					
					
					this.chatRoom.getOnlineUserList().setText(users);
					this.chatRoom.showUserList(userList);
					
					
//					String str = this.chatRoom.getOnlineUserList().getText();
//					System.out.println(str);
					
					
				}
				else if(type == 3){
					String serverMessage = XMLUtil.extractContent(xml);
					this.chatRoom.getChatRoomBox().append(serverMessage+"\n");
				}
				else if(type == 6){
					JOptionPane.showMessageDialog(this.chatRoom.frame, "���������ѹرգ������˳�","��Ϣ",JOptionPane.WARNING_MESSAGE);
					System.exit(0);
				}
				else if(type == 7){
					try{
						this.getSocket().close();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						System.exit(0);
					}
					
					
				}
				
				
								
				
				
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}

}
