import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

public class FeedbackServer {
	private final static int PORT = 17524;


	public static void main(String[] args) {
		FeedbackServer mb = new FeedbackServer();
		mb.listen();
	}
	
	

	
	/**
	 * Konstruktor ki ne dela niè
	 */
	public FeedbackServer(){
	}
	

	
	/**
	 * XML listener
	 * Posluša na portu 17525
	 * Dobi lahko 2 vrsti poizvedb: 
	 * 1.)Poizvedba o seznamu igralcev (lobby) + ali ima igralec morda kakšen request?
	 * 2.)Pošiljanje zahtevka za novo igro (aka povezavo k nekemu igralcu)
	 */
	public void listen() {
		while(true){
		try {
			ServerSocket listener = new ServerSocket(PORT);
			
			Socket lobby;
			System.out.println("Feedback se je zagnal....");
			
			while (true) {
				lobby = listener.accept();
				handleConnection(lobby);				
			}

		} catch (IOException ioe) {
			System.out.println("IOException: " + ioe);
			ioe.printStackTrace();
		}
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
	String pros = doc.getElementsByTagName("pros").item(0).getTextContent();
	String cons = doc.getElementsByTagName("cons").item(0).getTextContent();	
	
	String output = null;

		
		// ustvari XML sporocilo - za demo bo dovolj kar enakega kot prej
	try {
		output = serializeDocumentToString(buildResponseDocument("0"));
	} catch (ParserConfigurationException e) {
		e.printStackTrace();
	}
	
	out.writeUTF(output);


	
//	System.out.println("Ime osebe je: " + imeOsebe + " "+ idosebe+"  "+type);	

    server.close();
    
    PrintWriter prosW=new PrintWriter(new FileWriter("pros.txt",true));
    PrintWriter consW=new PrintWriter(new FileWriter("cons.txt",true));
    
    prosW.write(pros);
    consW.write(cons);
    
    prosW.println();
    prosW.println("-------------------------");
    
    consW.println();
    consW.println("-------------------------");
     
    prosW.close();
    consW.close();
    
}

/**
	 * Create XML document (odgovor streznika odjemalcu)
	 * 
	 * @return Document
	 * @throws ParserConfigurationException
	 */
	private static Document buildResponseDocument(String sporocilo) throws ParserConfigurationException {
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = fact.newDocumentBuilder();
		Document doc = parser.newDocument();
	
		Node root = doc.createElement("sporocilo2");
		doc.appendChild(root);
		
		Node tip = doc.createElement("sporocilo");
		root.appendChild(tip);
		
		
		tip.appendChild(doc.createTextNode(sporocilo));

		
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