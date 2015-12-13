import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;

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


public class GameServer extends Thread {
	private int PORT;
	private String PLAYER1;
	private String PLAYER2;
	
	private boolean currentPlayer = true; //true = player1
	
	private boolean firstPlayerStart = true;
	private boolean firstPlayerMove = true;
	private boolean active = true;
	private boolean FINISH = false;
	private boolean firstMove = true;
	private boolean handshaking = true;
	
	private boolean WON = false;
	private String WINNER="";
	
//	private boolean NEWGAME = false;
	private boolean player1NewGame = false;
	private boolean player2NewGame = false;
	
	private boolean player1Registered = false;
	private boolean player2Registered = false;
	
	private boolean player1END = false;
	private boolean player2END = false;
	
	private long player1Time = System.currentTimeMillis();
	private long player2Time = System.currentTimeMillis();
	
	private String player1Msg = "";
	private String player2Msg = "";
	
	private String X="";
	private String Y="";
	
	//////////////////////////////////////////////
	//////////////////////////////////////////////
	//////////////////////////////////////////////
	
	public GameServer(int PORT,String player1,String player2){
		this.PORT = PORT;
		this.PLAYER1 = player1;
//		this.PLAYER2 = player2;
	}
	
	public void run(){
		firstStart();
//		System.out.print(firstPlayerMove);
		listen();
	}
	
	public void firstStart(){
		if((int)(Math.random()*10) >= 5){
			firstPlayerMove = true;
			firstPlayerStart = true;
		}else{
			firstPlayerMove = false;
			firstPlayerStart = false;
		}
	}
	
	
	/**
	 * XML listener
	 * Posluša na portu 17525
	 * Dobi lahko 2 vrsti poizvedb: 
	 * 1.)Poizvedba o seznamu igralcev (lobby) + ali ima igralec morda kakšen request?
	 * 2.)Pošiljanje zahtevka za novo igro (aka povezavo k nekemu igralcu)
	 */
	public void listen() {
			ServerSocket listener = null;
			
			try {
				listener = new ServerSocket(PORT);
				listener.setSoTimeout(2000);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Socket lobby = null;

			System.out.println("Gameserver "+PORT+" se je zagnal....");
			
			
			while (active) {
				try{
				lobby = listener.accept();
				handleConnection(lobby);
				}catch(Exception e){}
				
				endGame();
				checkActive();
			}
			
			try{
			lobby.close();
			listener.close();
			finish();
			}catch(IOException e){}
	}
	
	/**
	 * 
	 * @param server
	 * @throws IOException
	 * 
	 * Hendla connectione
	 * Dve vrsti povezave:
	 * 1.)Poizvedba o seznamu igralcev
	 * 2.)Zahtevek za novo igro
	 */
	protected void handleConnection(Socket server) throws IOException {
	//odpri ustrezne vhodne in izhodne tokove za readUTF in writeUTF
	DataOutputStream out = new DataOutputStream(server.getOutputStream());
	DataInputStream in = new DataInputStream(server.getInputStream());

	//preberi XML, ki ga je poslal odjemalec
	String input = in.readUTF();
	
	// razcleni XML
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	Document doc = null;
	try {
		doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(input)));
	} catch (SAXException e) {
		e.printStackTrace();
		
	} catch (IOException e) {
		e.printStackTrace();
		
	} catch (ParserConfigurationException e) {
		e.printStackTrace();
		
	}


	String idosebe = doc.getElementsByTagName("id").item(0).getTextContent();	
	String type = doc.getElementsByTagName("type").item(0).getTextContent();
	String chat = doc.getElementsByTagName("chat").item(0).getTextContent();
	
	
	if(idosebe.equalsIgnoreCase(PLAYER1)) currentPlayer = true;
	else currentPlayer = false;
	
	if(currentPlayer) player1Time = System.currentTimeMillis();
	else player2Time = System.currentTimeMillis();
	
	if(chat.equalsIgnoreCase("yes")){
		String pogovor = doc.getElementsByTagName("pogovor").item(0).getTextContent();
		if(currentPlayer) player2Msg += pogovor;
		else player1Msg += pogovor;
	}
	
	
	if(FINISH){
		if(currentPlayer) player1END = true;
		else player2END = true;
		
		String output = null;
		try {
			output = serializeDocumentToString(buildResponseDocument("end","Your opponent has quit.","0","0"));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			
		}
		
		out.writeUTF(output);
	}else if(handshaking){
		if(currentPlayer) player1Registered = true;
		else{ player2Registered = true;
			PLAYER2 = idosebe;
		}
		if(player1Registered && player2Registered) handshaking = false;
		
		String handEnd = doc.getElementsByTagName("sporocilo").item(0).getTextContent();
		if(handEnd.equalsIgnoreCase("end")){
			if(currentPlayer) player1END = true;
			else player2END = true;
			
			FINISH = true;
			handshaking = false;
			
			String output = null;
			try {
				output = serializeDocumentToString(buildResponseDocument("end","You have quit.","0","0"));
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
				
			}
			
			out.writeUTF(output);
		}else{
			
			String output = null;
			try {
				output = serializeDocumentToString(buildResponseDocument("handshaking","Waiting for other player.","0","0"));
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
				
			}
			
			out.writeUTF(output);
		}
	}else if(WON && WINNER != idosebe){
		WON = false;
		String output = null;
		try {
			output = serializeDocumentToString(buildResponseDocument("won","yes",X,Y));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			
		}
		
		out.writeUTF(output);
		
//		if(currentPlayer){
//			if(player1WON){
//				String output = null;
//				try {
//					output = serializeDocumentToString(buildResponseDocument("won","yes",X,Y));
//				} catch (ParserConfigurationException e) {
//					e.printStackTrace();
//					
//				}
//				
//				out.writeUTF(output);
//			}else{
//				String output = null;
//				try {
//					output = serializeDocumentToString(buildResponseDocument("won","no",X,Y));
//				} catch (ParserConfigurationException e) {
//					e.printStackTrace();
//					
//				}
//				
//				out.writeUTF(output);
//			}
//				
//		}else{
//			if(player1WON){
//				String output = null;
//				try {
//					output = serializeDocumentToString(buildResponseDocument("won","no",X,Y));
//				} catch (ParserConfigurationException e) {
//					e.printStackTrace();
//					
//				}
//				
//				out.writeUTF(output);
//			}else{
//				String output = null;
//				try {
//					output = serializeDocumentToString(buildResponseDocument("won","yes",X,Y));
//				} catch (ParserConfigurationException e) {
//					e.printStackTrace();
//					
//				}
//				
//				out.writeUTF(output);
//			}
//		}
	}else{
	
	
	if(type.equalsIgnoreCase("end")){
		if(currentPlayer) player1END = true;
		else player2END = true;
		
		FINISH = true;
		
		String output = null;
		try {
			output = serializeDocumentToString(buildResponseDocument("end","You have quit.","0","0"));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			
		}
		
		out.writeUTF(output);
		
	}else if(type.equals("waiting")){
		
		if(player1NewGame && player2NewGame){
			String output = null;
			try {
				output = serializeDocumentToString(buildResponseDocument("new_game","ok","0","0"));
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
				
			}
			out.writeUTF(output);
		}else if(player1NewGame && !player2NewGame){
			if(currentPlayer){
				String output = null;
				try {
					output = serializeDocumentToString(buildResponseDocument("waiting","ok","0","0"));
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
					
				}
				out.writeUTF(output);
			}else{
				String output = null;
				try {
					output = serializeDocumentToString(buildResponseDocument("waiting","new_game","0","0"));
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
					
				}
				out.writeUTF(output);
			}
		}else if(!player1NewGame && player2NewGame){
			if(currentPlayer){
				String output = null;
				try {
					output = serializeDocumentToString(buildResponseDocument("waiting","new_game","0","0"));
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
					
				}
				out.writeUTF(output);
			}else{
				String output = null;
				try {
					output = serializeDocumentToString(buildResponseDocument("waiting","ok","0","0"));
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
					
				}
				out.writeUTF(output);
			}
			
		}else if(!player1NewGame && !player2NewGame){
			String output = null;
			try {
				output = serializeDocumentToString(buildResponseDocument("waiting","ok","0","0"));
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
				
			}
			out.writeUTF(output);
		}
		
//		if(!NEWGAME){
//		String output = null;
//		try {
//			output = serializeDocumentToString(buildResponseDocument("waiting","ok","0","0"));
//		} catch (ParserConfigurationException e) {
//			e.printStackTrace();
//			
//		}
//		out.writeUTF(output);
//		}else{
//			NEWGAME = false;
//			String output = null;
//			try {
//				output = serializeDocumentToString(buildResponseDocument("new_game","Opponent requested new game","0","0"));
//			} catch (ParserConfigurationException e) {
//				e.printStackTrace();
//				
//			}
//			out.writeUTF(output);
//		}
	}else if(type.equals("request")){
		if(currentPlayer){
			if(firstPlayerMove){
				if(!firstMove){
				String output = null;
				try {
					output = serializeDocumentToString(buildResponseDocument("request","yes",X,Y));
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
					
				}
				
				out.writeUTF(output);
				}else{
					
					String output = null;
					try {
						output = serializeDocumentToString(buildResponseDocument("request","true","0","0"));
					} catch (ParserConfigurationException e) {
						e.printStackTrace();
						
					}
					
					out.writeUTF(output);
				}
			}else{
				String output = null;
				try {
					output = serializeDocumentToString(buildResponseDocument("request","no","0","0"));
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
					
				}
				
				out.writeUTF(output);
			}
		}else{
			if(!firstPlayerMove){
				if(!firstMove){
					
					String output = null;
					try {
						output = serializeDocumentToString(buildResponseDocument("request","yes",X,Y));
					} catch (ParserConfigurationException e) {
						e.printStackTrace();
						
					}
					
					out.writeUTF(output);
					}else{
						
						String output = null;
						try {
							output = serializeDocumentToString(buildResponseDocument("request","true","0","0"));
						} catch (ParserConfigurationException e) {
							e.printStackTrace();
							
						}
						
						out.writeUTF(output);
					}
				}else{
					String output = null;
					try {
						output = serializeDocumentToString(buildResponseDocument("request","no","0","0"));
					} catch (ParserConfigurationException e) {
						e.printStackTrace();
						
					}
					
					out.writeUTF(output);
				}
		}
	}else if(type.equalsIgnoreCase("move")){//ce gre za reqest
		firstMove = false;
		
		X = doc.getElementsByTagName("X").item(0).getTextContent();
		Y = doc.getElementsByTagName("Y").item(0).getTextContent();
		
		//Da nasprotniku potezo
		if(currentPlayer) firstPlayerMove = false;
		else firstPlayerMove = true;
		
		String output = null;
		try {
			output = serializeDocumentToString(buildResponseDocument("request","ok","0","0"));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			
		}
		
		out.writeUTF(output);
		
	}else if(type.equals("new_game")){
		//TODO
		if(currentPlayer)player1NewGame = true;
		else player2NewGame = true;
		
		if(!firstMove){
			firstPlayerMove = !firstPlayerStart;
			firstPlayerStart = firstPlayerMove;
			firstMove = true;
		}
		
		String output = null;
		try {
			output = serializeDocumentToString(buildResponseDocument("waiting","wait_for_opponent","0","0"));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			
		}
		
		out.writeUTF(output);
	}else{//if type = won
		WON = true;
		
		player1NewGame = false;
		player2NewGame = false;
		
		X = doc.getElementsByTagName("X").item(0).getTextContent();
		Y = doc.getElementsByTagName("Y").item(0).getTextContent();
		
		WINNER = idosebe;
		
		String output = null;
		try {
			output = serializeDocumentToString(buildResponseDocument("waiting","ok","0","0"));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			
		}
		
		out.writeUTF(output);
	}
	}
	
//	System.out.println("GM: " + imeOsebe + " "+ idosebe+"  "+type);	

	

    server.close();
}

/**
	 * Create XML document (odgovor streznika odjemalcu)
	 * 
	 * @return Document
	 * @throws ParserConfigurationException
	 */
	private Document buildResponseDocument(String type,String sporocilo,String X,String Y) throws ParserConfigurationException {
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = fact.newDocumentBuilder();
		Document doc = parser.newDocument();
	
		Node root = doc.createElement("sporocilo");
		doc.appendChild(root);
		
		Node tip = doc.createElement("type");
		root.appendChild(tip);
		
		Node msg = doc.createElement("msg");
		root.appendChild(msg);
		
		Node x = doc.createElement("X");
		root.appendChild(x);
		
		Node y = doc.createElement("Y");
		root.appendChild(y);
		
		tip.appendChild(doc.createTextNode(type));
		msg.appendChild(doc.createTextNode(sporocilo));
		x.appendChild(doc.createTextNode(X));
		y.appendChild(doc.createTextNode(Y));
		
		if(currentPlayer){
			if(player1Msg.length() > 0){
				Node chat = doc.createElement("chat");
				root.appendChild(chat);
				
				chat.appendChild(doc.createTextNode("yes"));
				
				Node pogovor = doc.createElement("pogovor");
				root.appendChild(pogovor);
				
				pogovor.appendChild(doc.createTextNode(player1Msg));
				
				player1Msg = "";
			}else{
				Node chat = doc.createElement("chat");
				root.appendChild(chat);
				
				chat.appendChild(doc.createTextNode("no"));
			}
		}else{
			if(player2Msg.length() > 0){
				Node chat = doc.createElement("chat");
				root.appendChild(chat);
				
				chat.appendChild(doc.createTextNode("yes"));
				
				Node pogovor = doc.createElement("pogovor");
				root.appendChild(pogovor);
				
				pogovor.appendChild(doc.createTextNode(player2Msg));
				
				player2Msg = "";
			}else{
				Node chat = doc.createElement("chat");
				root.appendChild(chat);
				
				chat.appendChild(doc.createTextNode("no"));
			}
		}
		
		return doc;
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

	private void endGame(){
		if(player1END && player2END){
			active = false;	
		}
	}
	
	private void finish(){
		MindblastLobby.porti.push(PORT);
		System.out.println("ENDING SERVER: "+PORT);
	}

	private void checkActive(){
		if((player1Time-System.currentTimeMillis()+25000) < 0){
			player1END = true; //timedout
//			if(!player1Registered) FINISH = true;
			FINISH = true;
		}
		if((player2Time-System.currentTimeMillis()+25000) < 0){
			player2END = true; //timedout
//			if(!player2Registered) FINISH = true;
			FINISH = true;
		}
	}
}
