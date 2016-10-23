package zixue.hyl.chat2.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import zixue.hyl.chat2.client.ChatRoom;
import zixue.hyl.chat2.client.Client;
import zixue.hyl.chat2.util.XMLUtil;

public class ServerMessageThread extends Thread
{
	private Server server;
	private Client client;
	private InputStream is;
	private OutputStream os;
	
	
	

	public ServerMessageThread(Server server,Socket socket){
		this.server = server;
		try
		{
			this.is = socket.getInputStream();
			this.os = socket.getOutputStream();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	
	
	public void updateUserList(){
		//更新用户列表
		//首先更新服务器端的用户列表
		Set<String> usernames = this.server.getMap().keySet();
		
		
		String userList = "";
		for(String username:usernames){
			userList += username+"\n";
//			
		}
		
		this.server.getUserList().setText(userList);
		
		
		
		
		//接下来更新客户端的用户列表
		//更新客户端用户列表与更新服务器端用户列表不同，因为服务器端本身维护着一个用户的列表所以只需要将用户名设置一下就好了
		//然而更新客户端的用户列表时，因为客户端有很多个，所以要对各个客户端进行遍历，且所有用户的集合是在服务器端，所以要将这样的集合发送到客户端才行
		//这样更新客户端用户列表就涉及到了网络通信，而网络通信数据的传输是靠XML进行传输的，所以需要构造用户列表的xml，然后通过ServerMessageThread发送
		String xml = XMLUtil.consructUserListXML(usernames);
		
		Collection<ServerMessageThread> cols = this.server.getMap().values();
		for(ServerMessageThread smt:cols){
			smt.sendMessage(xml);//向每个线程发送在线用户列表的xml数据
		}
		
		
	}
	
	public void sendMessage(String message){
		try
		{
			os.write(message.getBytes());
		}
		catch (IOException e)
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
				
				String message = new String(buf,0,length);
				int type = Integer.parseInt(XMLUtil.extractType(message));
				
				String user = XMLUtil.extractUsername(message);
				String toUser = XMLUtil.extract2Username(message);
				System.out.println(toUser);
				
				if(type == 2){
					
					String messageContent = XMLUtil.extractContent(message);
					
					String serverMessageContent = user +":" + messageContent;
					String serverMessageXML = XMLUtil.constructServerMessage(serverMessageContent);
					
					Map<String,ServerMessageThread> map = this.server.getMap();
					Collection<ServerMessageThread> cols = map.values();//集合cols中是所有的用户
					
					ServerMessageThread smt = map.get(toUser);
					if(user.equals(toUser)){
						JOptionPane.showMessageDialog(this.server.frame, "不能向自己发送信息","信息",JOptionPane.WARNING_MESSAGE);
					}else{
						smt.sendMessage(serverMessageXML);
					}
					
					ServerMessageThread smt2 = map.get(user);
					if(user.equals(toUser)){
						JOptionPane.showMessageDialog(this.server.frame, "不能向自己发送信息","信息",JOptionPane.WARNING_MESSAGE);
					}else{
						smt2.sendMessage(serverMessageXML);
					}
					
					
					
//					for(ServerMessageThread smt:cols){
//						
//						smt.sendMessage(serverMessageXML);
//					}
				}
				else if(type == 5){
					ServerMessageThread smt = this.server.getMap().get(user);
					
					String msg = XMLUtil.constuctClientCloseWindowConfirmation();
					smt.sendMessage(msg);
					
					this.server.getMap().remove(user);
					this.updateUserList();
					
					this.is.close();
					this.os.close();
					
					break;
					
				}
				
				
				
				
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		
	}

}
