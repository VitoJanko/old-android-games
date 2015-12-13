package com.mindblast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringReader;
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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.os.Handler;
import android.os.Message;

public class LoadLobby extends Thread {
	protected String host;
	protected int port;
	
	String errorMsg = "";

	boolean active = true;
	Lobby lobbyActivity;
	Handler lobbyHandler;
	
	public LoadLobby(String host,int port,Handler lobbyHandler,Lobby lobbyActivity){
		this.host = host;
		this.port = port;
		this.lobbyHandler = lobbyHandler;
		this.lobbyActivity = lobbyActivity;
	}
	
	public void run(){
		while(active){
			connect();
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 
	 * @param seznam je string vseh besed pobranih iz SQL baze, loèeni so z </br//>
	 * Najprej se izbriše trenutna vsebina seznama "seznami", tako da so podatki ažurni.
	 * Obdela seznam besed, loèi se vsako besedo (oz osebo) v svojo tabelico
	 * in nato pošlje v Lobby kjer se prikaže na ekranu.
	 * 
	 * Preveri se tudi, da se ne doda igralca v tabelo, da tako ne more samega nase kliknt.
	 * 
	 * Z message handlerjem se pošlje sporoèilo v Lobby, da so bili podatki spremenjeni in jih je potrebno updejtat.
	 * 
	 */
	public void obdelajPodatke(String seznam){
		if(seznam.length() > 0){
		
			String[] igralci = seznam.split("</br//>");

			lobbyActivity.nasprotniki.clear();
			
			for(int i=0;i<igralci.length;i++){
				String[] igralec = igralci[i].split("<-->");
				if(!igralec[0].equalsIgnoreCase(MainMenu.ID))
					lobbyActivity.nasprotniki.add(new Opponent(igralec[0],igralec[1],"score",R.drawable.smejkotest));
			}
				       
        
        Message msg = new Message();
        msg.what = 0;
        lobbyHandler.sendMessage(msg);
		}
	}
	
	public void obdelajRequeste(String requesti){
		//TODO
		String[] req = requesti.split("</br//>");
		
		for(int i=0;i<req.length;i++){
			String[] port = req[i].split("<-->");
			lobbyActivity.player2 = port[0];
			lobbyActivity.PORT = Integer.valueOf(port[1]);
		}
	}
	
	
	/**
	 * Poveže se na strežnik "mindblastLobby"
	 */
	public void connect() {
		try {
			SocketAddress sockaddr = new InetSocketAddress(host, port);
			Socket sock = new Socket();
			sock.connect(sockaddr, 10000);
			handleConnection(sock);
		}catch(Exception e){
			errorMsg = "You have problems with connection. Server is offline! Or your internet connection is broken.";
	        Message msg = new Message();
	        msg.what = 2;
	        lobbyHandler.sendMessage(msg);
		}
	}

	/**
	 * Poveže se na strežnik, kateremu pošlje ID igralca z katerim hoèemo igrati.
	 * ID smo dobili, ko smo v Lobby kliknini na njega.
	 * @param reqID ID igralca z katerim hoèemo igrati.
	 */
	public void connect(String reqID){
		try {
			SocketAddress sockaddr = new InetSocketAddress(host, port);
			Socket sock = new Socket();
			sock.connect(sockaddr, 50000);
			handleConnection(sock,reqID);
		}catch(Exception e){
			errorMsg = "You have problems with connection. Server is offline! Or your internet connection is broken.";
	        Message msg = new Message();
	        msg.what = 2;
	        lobbyHandler.sendMessage(msg);
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
		String odgovor = in.readUTF();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document docreturn = null;
		try {
			docreturn = factory.newDocumentBuilder().parse(new InputSource(new StringReader(odgovor)));
		} catch (SAXException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.exit(1);
		}

		//dobi seznam vn
		String seznam = docreturn.getElementsByTagName("sporocilo").item(0).getTextContent();
		//prebere seznam z requesti (kateri igralci želijo igrat iz njim
		String request = docreturn.getElementsByTagName("request").item(0).getTextContent();
	
		client.close();
		
		//obdela podatke iz seznama igralcev ter jih prikaže na zaslon v Lobbyu
		obdelajPodatke(seznam);
		
		//Pogleda ali je kakšen request za igranje z njim
		if(request.length() > 2){
//			Lobby.PORT = Integer.valueOf(request);
			obdelajRequeste(request);
			
	        Message msg = new Message();
	        msg.what = 1;
	        lobbyHandler.sendMessage(msg);
		}
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

		Node id = doc.createElement("id");
		oseba.appendChild(id);

		Node ime = doc.createElement("ime");
		oseba.appendChild(ime);
		
		Node type= doc.createElement("type");
		oseba.appendChild(type);

		id.appendChild(doc.createTextNode(MainMenu.ID));
		ime.appendChild(doc.createTextNode(MainMenu.IME));
		type.appendChild(doc.createTextNode("renewal"));
						
		return doc;
	}

	
	/**
	 * 
	 * @param client
	 * @param requestID ID igralca z katerim želimo igrati
	 * @throws IOException
	 */
	protected void handleConnection(Socket client,String requestID) throws IOException {
		DataOutputStream out = new DataOutputStream(client.getOutputStream());
		DataInputStream in = new DataInputStream(client.getInputStream());
		
		//Pošlje podatke
		Document doc = null;
		try {
			doc = buildXMLRequest(requestID);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.exit(1);
		}
		String content = serializeDocumentToString(doc);
		out.writeUTF(content);

		//Prebere podatke
		String odgovor = in.readUTF();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document docreturn = null;
		try {
			docreturn = factory.newDocumentBuilder().parse(new InputSource(new StringReader(odgovor)));
		} catch (SAXException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.exit(1);
		}

		//dobi seznam vn
		String seznam = docreturn.getElementsByTagName("sporocilo").item(0).getTextContent();
		//dobi request, èe kdo želi igraz z njim
		String request = docreturn.getElementsByTagName("request").item(0).getTextContent();
		//Èe je server poln dobi error vn.
		String error = docreturn.getElementsByTagName("error").item(0).getTextContent();

		client.close();
		
		//Obdela podatke iz seznama
		obdelajPodatke(seznam);
		
		//Èe ni errorja, zažene igro.
		if(error.length() < 2){
			Lobby.PORT = Integer.valueOf(request);
			lobbyActivity.newGame();

		}else{//Èe je error , izpiše error.
			errorMsg = "Server is full!";
	        Message msg = new Message();
	        msg.what = 2;
	        lobbyHandler.sendMessage(msg);
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
	private Document buildXMLRequest(String requestID) throws ParserConfigurationException {
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = fact.newDocumentBuilder();
		Document doc = parser.newDocument();

		Node oseba = doc.createElement("oseba");
		doc.appendChild(oseba);

		Node id = doc.createElement("id");
		oseba.appendChild(id);

		Node ime = doc.createElement("ime");
		oseba.appendChild(ime);
		
		Node type= doc.createElement("type");
		oseba.appendChild(type);
		
		Node requestid = doc.createElement("requestID");
		oseba.appendChild(requestid);

		id.appendChild(doc.createTextNode(MainMenu.ID));
		ime.appendChild(doc.createTextNode(MainMenu.IME));
		type.appendChild(doc.createTextNode("request"));
		requestid.appendChild(doc.createTextNode(requestID));
						
		return doc;
	}
	
}
