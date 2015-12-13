package com.mindblast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;


public class Feedback extends Thread {
	protected String host = "188.230.131.244";
	protected int port = 17524;
	
	String pros ="";
	String cons ="";
	
	public Feedback(String pros,String cons){
		this.pros = pros;
		this.cons = cons;
	}
	
	public void run(){
		connect();
	}
	
	
	
	/**
	 * Poveže se na strežnik "mindblastLobby"
	 */
	public void connect() {
		try {
			SocketAddress sockaddr = new InetSocketAddress(host, port);
			Socket sock = new Socket();
			sock.connect(sockaddr, 2000);
			handleConnection(sock);
		}catch(Exception e){
		}
	}

	
	/**
	 * HandleContection
	 * @param client
	 * @throws IOException
	 */
	protected void handleConnection(Socket client) throws IOException {
		DataOutputStream out = new DataOutputStream(client.getOutputStream());
		DataInputStream in = new DataInputStream(client.getInputStream());
		
		//Pošlje poizvedbo
		Document doc = null;
		try {
			doc = buildXMLRequest();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.exit(1);
		}
		String content = serializeDocumentToString(doc);
		out.writeUTF(content);

		//Prejme odgovor
		@SuppressWarnings("unused")
		String odgovor = in.readUTF();
	
		client.close();

	}

	/** 
	 * convert Document to String
	 * http://www.theserverside.com/discussions/thread.tss?thread_id=26060
	 */

	private String serializeDocumentToString(Document doc) {
		try {
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			return writer.toString();
		} catch (TransformerException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Create XML document
	 * @param znesek 
	 * @param operacija 
	 * @return Document
	 * @throws ParserConfigurationException
	 * 
	 * Naredi XML datoteko z podatki o koordinatah X in Y, ostali podatki so zaenkrat le balast, ki se bodo uporabili kasneje v razvoju ...
	 */
	private Document buildXMLRequest() throws ParserConfigurationException {
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = fact.newDocumentBuilder();
		Document doc = parser.newDocument();

		Node oseba = doc.createElement("oseba");
		doc.appendChild(oseba);

		Node id = doc.createElement("pros");
		oseba.appendChild(id);

		Node ime = doc.createElement("cons");
		oseba.appendChild(ime);
		


		id.appendChild(doc.createTextNode(pros));
		ime.appendChild(doc.createTextNode(cons));
						
		return doc;
	}	
}
