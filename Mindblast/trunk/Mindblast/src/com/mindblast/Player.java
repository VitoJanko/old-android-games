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

public class Player extends Thread  {
	
	
	////////////////////
	/////CLIENT/////////
	////////////////////
	protected String host;
	protected int port;
	protected TicTacToeView view;
	
	//String type,String name,String ID,String X,String Y,String sporocilo
	public String type = "request";
	public String name = MainMenu.IME;
	public String ID = MainMenu.ID;
	public String X = "0";	// koordinate se zamenjata v poslji/won
	public String Y = "0";
	public String sporocilo= "0";
	
	public static boolean active = true;	//ko = false, se zanka izteèe
	


	public Player(String host, int port,TicTacToeView view) {
		this.host = host;
		this.port = port;
		this.view = view;
	}
	
	/**
	 * 
	 * @param X koordinate
	 * @param Y koordinate
	 * 
	 * V naslednjem sporocilu se pod "TYPE = MOVE" posljeta koordinati.
	 */
	public void poslji(int X,int Y){
		type = "move";
		this.X = String.valueOf(X);
		this.Y = String.valueOf(Y);
	}
	
	/**
	 * 
	 * @param X koordinate
	 * @param Y koordinate
	 * 
	 * Igralec je zmagal. V naslednjem sporocilu se posljeta koordinati pod "TYPE = WON".
	 */
	public void won(int X,int Y){
		type = "won";
		this.X = String.valueOf(X);
		this.Y = String.valueOf(Y);
	}
	
	public void new_game(){
		type = "new_game";
	}
	
	public void end(){
		type = "end";
		sporocilo = "end";
	}
	
	
	/**
	 * Teèe dokler je active. Usakic zaspi za 0.4s
	 */
	public void run(){
		while(active){
			connect();
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/** Establishes the connection, then passes the socket
	 *  to handleConnection.
	 * @param znesek 
	 * @param operacija 
	 * @param  
	 * @param vrsta 
	 */
	public void connect() {
//			Socket client = new Socket(host, port);
//			handleConnection(client);		
		try {
			SocketAddress sockaddr = new InetSocketAddress(host, port);
			Socket sock = new Socket();
			sock.connect(sockaddr, 5000);
			handleConnection(sock);
		}catch(Exception e){
			view.endGame();
		}
	}

	/** 
	 * Podroben opis je pri posameznem stavku.
	 */
	protected void handleConnection(Socket client) throws IOException {
		DataOutputStream out = new DataOutputStream(client.getOutputStream());
		DataInputStream in = new DataInputStream(client.getInputStream());

		//Poslje odgovor serverju
		Document doc = null;
		try {
			doc = buildXMLRequest();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}		
		String content = serializeDocumentToString(doc);	
		out.writeUTF(content);
		
		//Prebere odgovor serverja
		String odgovor = in.readUTF();	
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document docreturn = null;
		try {
			docreturn = factory.newDocumentBuilder().parse(new InputSource(new StringReader(odgovor)));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		// Prebere se tip in sporocilo xml-ja. 
		String tip = docreturn.getElementsByTagName("type").item(0).getTextContent();
		String msg = docreturn.getElementsByTagName("msg").item(0).getTextContent();
		String chat = docreturn.getElementsByTagName("chat").item(0).getTextContent();
		
		if(chat.equalsIgnoreCase("yes")){
			String pogovor = docreturn.getElementsByTagName("pogovor").item(0).getTextContent();
//			TicTacToe.zamenjajText("nasprotnik", pogovor);
			view.obdelajMsg(true, pogovor);
		}
		
		//Èe je tip "end" je nasprotnik zakljucil igro. 
		if(tip.equalsIgnoreCase("end")){
			view.endGame();
			//Èe je tip WON, je igralec/nasportnik zmagal. Preberejo se koordinate ter ponovno narisejo
		}else if(tip.equalsIgnoreCase("won")){
			type = "waiting";
			if(msg.equalsIgnoreCase("yes")){
				String enemyX = docreturn.getElementsByTagName("X").item(0).getTextContent();
				String enemyY = docreturn.getElementsByTagName("Y").item(0).getTextContent();
				
				view.zamenjajPlosco(Integer.valueOf(enemyX), Integer.valueOf(enemyY), 2);
			}//else{
//				String enemyX = docreturn.getElementsByTagName("X").item(0).getTextContent();
//				String enemyY = docreturn.getElementsByTagName("Y").item(0).getTextContent();
//				
//				view.zamenjajPlosco(Integer.valueOf(enemyX), Integer.valueOf(enemyY), 2);
//			}
		}else if(tip.equalsIgnoreCase("new_game")){
			type = "request";
			view.krizec = false;
			view.playAgain = false;
			view.waiting = false;
			view.newGame();
			view.startTimer();
			
		}else if(tip.equalsIgnoreCase("waiting")){
			type = "waiting";
			if(msg.equalsIgnoreCase("new_game")){
				view.playAgain = true;
			}else if(msg.equalsIgnoreCase("waiting")){
				view.playAgain = true;
			}else if(msg.equalsIgnoreCase("wait_for_opponent")){
				view.waiting = true;
				view.playAgain = true;
			}
		}else if(tip.equalsIgnoreCase("request")){//Standardni request: Èe je yes pomeni da je igralec na vrsti, preberejo se koordinate.
			view.waiting = false;
			view.END = false;
			view.playAgain = false;
			
			//Touchlistener se omogoèi, saj je sedaj igralec na vrsti.
			if(msg.equalsIgnoreCase("yes")){
				String enemyX = docreturn.getElementsByTagName("X").item(0).getTextContent();
				String enemyY = docreturn.getElementsByTagName("Y").item(0).getTextContent();
			
				view.zamenjajPlosco(Integer.valueOf(enemyX), Integer.valueOf(enemyY), 2);
				view.krizec = true;
				//True se pojavi samo prviè v igri, ko noben od igralcev ni še igral. Pove nam, da igralec zaène
				//Ker še nihèe ni igral se koordinate ne preberejo.
			}else if(msg.equalsIgnoreCase("true")){
				view.krizec = true;
				
				//Ko posljemo koordinate dobimo nazaj odgovor OK ki nam pove, da jih je strežnik prejel. Èe odgovora ne dobimo
				//se ponovno pošljejo v naslednji sinhronizaciji.
			}else if(msg.equalsIgnoreCase("ok")){
				type = "request";
				view.krizec = false;
			}else{
				type = "request";
				view.krizec = false;
			}
		}else if(tip.equalsIgnoreCase("handshaking")){
			view.waiting = true;
			view.END = true;
			view.playAgain = true;
			view.startTimer();
		}
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
	 * Podatke prebere it spremenljivk razreda this.Player
	 */
	private Document buildXMLRequest() throws ParserConfigurationException {
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = fact.newDocumentBuilder();
		Document doc = parser.newDocument();

		Node oseba = doc.createElement("oseba");
		doc.appendChild(oseba);

		Node id = doc.createElement("id");
		oseba.appendChild(id);

		Node i = doc.createElement("ime");
		oseba.appendChild(i);
		
		Node t= doc.createElement("type");
		oseba.appendChild(t);
		
		Node x= doc.createElement("X");
		oseba.appendChild(x);
		
		Node y= doc.createElement("Y");
		oseba.appendChild(y);
		
		Node m= doc.createElement("sporocilo");
		oseba.appendChild(m);

		id.appendChild(doc.createTextNode(ID));
		i.appendChild(doc.createTextNode(name));
		t.appendChild(doc.createTextNode(type));
		x.appendChild(doc.createTextNode(X));
		y.appendChild(doc.createTextNode(Y));
		m.appendChild(doc.createTextNode(sporocilo));
		
		String mojeSporocilo = TicTacToe.myMsg(false, "");
		if(mojeSporocilo.length() > 0){
			Node chat = doc.createElement("chat");
			oseba.appendChild(chat);
			chat.appendChild(doc.createTextNode("yes"));
			
			Node pogovor = doc.createElement("pogovor");
			oseba.appendChild(pogovor);
			
			pogovor.appendChild(doc.createTextNode(mojeSporocilo));
		}else{
			Node chat = doc.createElement("chat");
			oseba.appendChild(chat);
			chat.appendChild(doc.createTextNode("no"));
		}
						
		return doc;
	}
}
