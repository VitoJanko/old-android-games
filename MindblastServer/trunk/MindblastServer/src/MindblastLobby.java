import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

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

public class MindblastLobby {
	private final static int PORT = 17525;
	public ArrayList<String[]> seznamIgralcev = new ArrayList<String[]>();
	String seznam="";
	public static Stack<Integer> porti = new Stack<Integer>();
	HashMap<String,String> requesti = new HashMap<String,String>();
	
	SQL sb;

	public static void main(String[] args) {
		MindblastLobby mb = new MindblastLobby();
		mb.zazeni();
	}
	
	
	/**
	 * 
	 * @param seznamIgralcev iz razreda SQL dobi seznam igralcev,
	 * ki ga shrani v STRING seznam, ta string se bo potem pošiljal naprej
	 */
	public void print(ArrayList<String[]> seznamIgralcev){
		this.seznamIgralcev = seznamIgralcev;
		
		seznam="";
		for(int i=0;i<seznamIgralcev.size();i++){
			String[] bb=seznamIgralcev.get(i);
			seznam += bb[0]+"<-->"+bb[1]+"</br//>";
		}
		
//		System.out.println(seznam);
	}
	
	/**
	 * Konstruktor ki ne dela niè
	 */
	public MindblastLobby(){
	}
	
	/**
	 * Zazene SQL razred ter XML listenerja.
	 */
	public void zazeni(){
		System.out.println("banananan");
		sb = new SQL(this);
		sb.start();
		
		for(int i=17526;i<19525;i++){
			porti.push(i);	
		}
				
		listen();
	}
	
	/**
	 * XML listener
	 * Posluša na portu 17525
	 * Dobi lahko 2 vrsti poizvedb: 
	 * 1.)Poizvedba o seznamu igralcev (lobby) + ali ima igralec morda kakšen request?
	 * 2.)Pošiljanje zahtevka za novo igro (aka povezavo k nekemu igralcu)
	 */
	public void listen() {
		try {
			ServerSocket listener = new ServerSocket(PORT);
			
			Socket lobby;
			System.out.println("Streznik se je zagnal....");
			
			while (true) {
				lobby = listener.accept();
				handleConnection(lobby);				
			}

		} catch (IOException ioe) {
			System.out.println("IOException: " + ioe);
			ioe.printStackTrace();
			System.exit(1);
		}
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
		System.exit(1);
	} catch (IOException e) {
		e.printStackTrace();
		System.exit(1);
	} catch (ParserConfigurationException e) {
		e.printStackTrace();
		System.exit(1);
	}

	// poisci ime osebe
	String imeOsebe = doc.getElementsByTagName("ime").item(0).getTextContent();
	String idosebe = doc.getElementsByTagName("id").item(0).getTextContent();	
	String type = doc.getElementsByTagName("type").item(0).getTextContent();
	
	if(type.equalsIgnoreCase("renewal")){

		//v SQL pošlje ime osebe in id		
		sb.insert(idosebe, imeOsebe);
		
		String output = null;
		String requestid = "0";
		
		if(requesti.containsKey(idosebe)){
			requestid = requesti.get(idosebe);
			requesti.remove(idosebe);
		}
		
		// ustvari XML sporocilo - za demo bo dovolj kar enakega kot prej
		try {
			output = serializeDocumentToString(buildResponseDocument(seznam,requestid,"0"));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		out.writeUTF(output);
		
	
	}else{//ce gre za reqest
		String requestID = doc.getElementsByTagName("requestID").item(0).getTextContent();
		String ime = doc.getElementsByTagName("ime").item(0).getTextContent();
		String output = null;
		
		//pogleda ali je še kak port na volji
		//èe je ga pošlje drugaèe pošlje error
		if(!porti.empty()){
			int tempPort = porti.pop();
			GameServer gm = new GameServer(tempPort,idosebe,requestID);
			gm.start();
			
			sb.deletPlayer(idosebe,requestID);
			
			if(requesti.containsKey(requestID)){
				String tempRequest = requesti.get(requestID);
				requesti.remove(requestID);
				tempRequest += ime+"<-->"+tempPort+"</br//>";
				requesti.put(requestID, tempRequest);
			}else{
				String tempReq = ime+"<-->"+tempPort+"</br//>";
				requesti.put(requestID,tempReq);
			}
			
			try {
				output = serializeDocumentToString(buildResponseDocument(seznam,String.valueOf(tempPort),"0"));
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		}else{
			try {
				output = serializeDocumentToString(buildResponseDocument(seznam,"0","Server Full"));
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		}
		
		out.writeUTF(output);
		
	}

	
//	System.out.println("Ime osebe je: " + imeOsebe + " "+ idosebe+"  "+type);	



    server.close();
}

/**
	 * Create XML document (odgovor streznika odjemalcu)
	 * 
	 * @return Document
	 * @throws ParserConfigurationException
	 */
	private static Document buildResponseDocument(String sporocilo,String request,String error) throws ParserConfigurationException {
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = fact.newDocumentBuilder();
		Document doc = parser.newDocument();
	
		Node root = doc.createElement("sporocilo2");
		doc.appendChild(root);
		
		Node tip = doc.createElement("sporocilo");
		root.appendChild(tip);
		
		Node msg = doc.createElement("request");
		root.appendChild(msg);
		
		Node err = doc.createElement("error");
		root.appendChild(err);
		
		tip.appendChild(doc.createTextNode(sporocilo));
		msg.appendChild(doc.createTextNode(request));
		err.appendChild(doc.createTextNode(error));
		
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

}