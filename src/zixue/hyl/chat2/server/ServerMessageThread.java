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
		//�����û��б�
		//���ȸ��·������˵��û��б�
		Set<String> usernames = this.server.getMap().keySet();
		
		
		String userList = "";
		for(String username:usernames){
			userList += username+"\n";
//			
		}
		
		this.server.getUserList().setText(userList);
		
		
		
		
		//���������¿ͻ��˵��û��б�
		//���¿ͻ����û��б�����·��������û��б�ͬ����Ϊ�������˱���ά����һ���û����б�����ֻ��Ҫ���û�������һ�¾ͺ���
		//Ȼ�����¿ͻ��˵��û��б�ʱ����Ϊ�ͻ����кܶ��������Ҫ�Ը����ͻ��˽��б������������û��ļ������ڷ������ˣ�����Ҫ�������ļ��Ϸ��͵��ͻ��˲���
		//�������¿ͻ����û��б���漰��������ͨ�ţ�������ͨ�����ݵĴ����ǿ�XML���д���ģ�������Ҫ�����û��б��xml��Ȼ��ͨ��ServerMessageThread����
		String xml = XMLUtil.consructUserListXML(usernames);
		
		Collection<ServerMessageThread> cols = this.server.getMap().values();
		for(ServerMessageThread smt:cols){
			smt.sendMessage(xml);//��ÿ���̷߳��������û��б��xml����
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
					Collection<ServerMessageThread> cols = map.values();//����cols�������е��û�
					
					ServerMessageThread smt = map.get(toUser);
					if(user.equals(toUser)){
						JOptionPane.showMessageDialog(this.server.frame, "�������Լ�������Ϣ","��Ϣ",JOptionPane.WARNING_MESSAGE);
					}else{
						smt.sendMessage(serverMessageXML);
					}
					
					ServerMessageThread smt2 = map.get(user);
					if(user.equals(toUser)){
						JOptionPane.showMessageDialog(this.server.frame, "�������Լ�������Ϣ","��Ϣ",JOptionPane.WARNING_MESSAGE);
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
