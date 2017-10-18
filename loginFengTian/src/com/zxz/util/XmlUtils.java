package com.zxz.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlUtils {

	private static List<Server> listServer = null;

	public static void main(String[] args) throws DocumentException {
		System.out.println(XmlUtils.getServerList("D:/apache-tomcat-8/webapps/loginServer","/config/servers.xml"));
	}

	public static List<Server> getServerList(String path,String serverPath)
			throws DocumentException {
		List<Server> listServer = new ArrayList<>();
		File file = new File(path + serverPath);
		SAXReader sr = new SAXReader();
		Document doc = sr.read(file);
		Element root = doc.getRootElement();
		Iterator<Element> it = root.elementIterator();
		while (it.hasNext()) {
			Element el = it.next();
			//输出id属性
			Iterator<Attribute> ait =  el.attributeIterator();
			Server ser = new Server();
			while(ait.hasNext()){
				Attribute attr = ait.next();
				String sserverId = attr.getName();
				if("id".equals(sserverId)){
					ser.setId(Integer.parseInt(attr.getText()));
				}
			}
			Iterator<Element> it2 = el.elementIterator();
			while (it2.hasNext()) {
				Element elem = it2.next();
				String name = elem.getName();
				if (name.equals("ip")) {
					ser.setIp(elem.getText());
				}
				if (name.equals("rpcPort")) {
					String sprot = elem.getText();
					ser.setRpcPort(Integer.parseInt(sprot));
				}
			}
			listServer.add(ser);
		}
		return listServer;
	}

	/**
	 * 一次性加载
	 * 
	 * @param path
	 * @return
	 * @throws DocumentException
	 */
	public static List<Server> getServerListOnece(String path)
			throws DocumentException {
		if (listServer == null) {
			File file = new File(path + "/config/servers.xml");
			SAXReader sr = new SAXReader();
			Document doc = sr.read(file);
			Element root = doc.getRootElement();
			Iterator<Element> it = root.elementIterator();
			listServer = new ArrayList<>();
			while (it.hasNext()) {
				Element el = it.next();
				Iterator<Element> it2 = el.elementIterator();
				Server ser = new Server();
				while (it2.hasNext()) {
					Element elem = it2.next();
					String name = elem.getName();
					if (name.equals("ip")) {
						ser.setIp(elem.getText());
					}
					if (name.equals("rpcPort")) {
						String sprot = elem.getText();
						ser.setRpcPort(Integer.parseInt(sprot));
					}
				}
				listServer.add(ser);
			}
		}
		return listServer;
	}

	/**
	 * 本地
	 * 
	 * @param path
	 * @return
	 * @throws DocumentException
	 */
	public static List<Server> getServerListLocal(String path)
			throws DocumentException {
		if (listServer == null) {
			File file = new File("src/config/servers.xml");
			SAXReader sr = new SAXReader();
			Document doc = sr.read(file);
			Element root = doc.getRootElement();
			Iterator<Element> it = root.elementIterator();
			listServer = new ArrayList<>();
			while (it.hasNext()) {
				Element el = it.next();
				Iterator<Element> it2 = el.elementIterator();
				Server ser = new Server();
				while (it2.hasNext()) {
					Element elem = it2.next();
					if (elem.getName().equals("ip")) {
						ser.setIp(elem.getText());
					}
					if (elem.getName().equals("port")) {
						ser.setIp(elem.getText());
					}
				}
				listServer.add(ser);
			}
		}
		return listServer;
	}

}
