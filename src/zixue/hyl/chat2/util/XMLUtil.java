package zixue.hyl.chat2.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;



public class XMLUtil
{
	public static Document constructDocument(){
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("message");
		document.setRootElement(root);
		return document;
	}
	
	/**
	 * 构建用户登录信息
	 */
	
	public static String constructLoginXML(String username){
		Document document = constructDocument();
		
		Element root = document.getRootElement();
		Element typeElement = root.addElement("type");
		typeElement.setText("1");
		
		Element userElement = root.addElement("user");
		userElement.setText(username);
		
		return document.asXML();
		
	}
	
	/**
	 * 解析出用户信息
	 */
	
	public static String extractType(String xml){
		try
		{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			
			Element type = document.getRootElement().element("type");
			return type.getText();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	

	
	public static String extractUsername(String xml){
		try
		{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			
			Element user = document.getRootElement().element("user");
			return user.getText();
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static String extract2Username(String xml){
		try
		{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			
			Element user = document.getRootElement().element("sendTo");
			return user.getText();
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	public static String consructUserListXML(Set<String> users){
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element typeElement = root.addElement("type");
		typeElement.setText("4");
		
		for(String user:users){
			Element userElement = root.addElement("user");
			userElement.setText(user);
		}
		
		return document.asXML();

	}
	
	public static List<String> extractUserList(String xml){
		List<String> list = new ArrayList<String>();
		try
		{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			
			
			for(Iterator<Element> userElements = document.getRootElement().elementIterator("user");userElements.hasNext();){
				list.add(userElements.next().getText());
//				Element e = (Element)userElements.next();
			}
			
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static String constructLoginResultXML(String loginResult){
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element typeElement = root.addElement("type");
		typeElement.setText("8");
		
		Element resultElement = root.addElement("result");
		resultElement.setText(loginResult);
		
		return document.asXML();
	}
	
	public static String extractLoginResult(String xml){
		
		try
		{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			
			Element loginResult = document.getRootElement().element("result");
			return loginResult.getText();
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String constructClientMessage(String message,String user,String toUser){
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element typeElement = root.addElement("type");
		typeElement.setText("2");
		
		Element userElement = root.addElement("user");
		userElement.setText(user);
		
		Element toUserElement = root.addElement("sendTo");
		toUserElement.setText(toUser);
		
		Element contentElement = root.addElement("content");
		contentElement.setText(message);
		
		return document.asXML();
	}
	
	public static String extractContent(String xml){
		
		try
		{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			
			Element clientMessageContent = document.getRootElement().element("content");
			return clientMessageContent.getText();
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static String constructServerMessage(String message){
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element typeElement = root.addElement("type");
		typeElement.setText("3");
		
		
		
		Element contentElement = root.addElement("content");
		contentElement.setText(message);
		
		return document.asXML();
	}
	
	//构建服务器端窗口关闭信息
	public static String constructServerClosingXML(){
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element typeElement = root.addElement("type");
		typeElement.setText("6");
		
		return document.asXML();
	}
	
	//构建客户端窗口关闭信息
	public static String constructClientClosingXML(String username){
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element typeElement = root.addElement("type");
		typeElement.setText("5");
		
		Element userElement = root.addElement("user");
		userElement.setText(username);
		
		return document.asXML();
	}
	
	public static String constuctClientCloseWindowConfirmation(){
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element typeElement = root.addElement("type");
		typeElement.setText("7");
		
		return document.asXML();
	}
}


