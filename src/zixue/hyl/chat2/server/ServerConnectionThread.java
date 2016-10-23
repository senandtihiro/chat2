package zixue.hyl.chat2.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import zixue.hyl.chat2.client.ChatRoom;
import zixue.hyl.chat2.server.ServerMessageThread;
import zixue.hyl.chat2.util.XMLUtil;

public class ServerConnectionThread extends Thread
{
	private Server server;
	private int port;
	private ServerSocket serverSocket;
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private ServerMessageThread serverMessageThread;
	
	
	
	public ServerConnectionThread(Server server,int port){
		try
		{
			this.server = server;
			serverSocket = new ServerSocket(port);
			
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
				socket = this.serverSocket.accept();
				is = socket.getInputStream();
				os = socket.getOutputStream();
				
				byte[] buf = new byte[5000];
				int length = is.read(buf);
				
				String loginXML = new String(buf,0,length);//服务器端读取客户端发送过来的登录信息
				String username = XMLUtil.extractUsername(loginXML);
				
				//判断用户是否登录成功
				String loginResult = null;
				boolean isLogin = false;
				if(this.server.getMap().containsKey(username)){
					loginResult = "failure";
				}
				else{
					loginResult = "success";
					isLogin = true;
				}
				
				String xml = XMLUtil.constructLoginResultXML(loginResult);
				os.write(xml.getBytes());//将登录结果反馈给客户端
				
				//如果登录成功，那么在开启处理聊天的线程的同时，要将该登录成功的用户添加到map中
				if(isLogin){
					
					ServerMessageThread serverMessageThread = new ServerMessageThread(this.server,socket);
					this.server.getMap().put(username, serverMessageThread);
					serverMessageThread.updateUserList();
					
					serverMessageThread.start();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}

}
