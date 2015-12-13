package com.mindblast;

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
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class EnemyMP extends Thread{
	private int port;
	TicTacToeView igraView;
	
	
	public EnemyMP(int port,TicTacToeView igraView){
		this.port = port;
		this.igraView = igraView;
		
		
		//this.listen();
	}
	
	public void makeMove(){
		
	}
	
	public void run(){
		this.listen();
		while(true){
			
		}
	}

	
	//SERVER JE OD TUKI NAPREJ
	///////////////////////////
	///////////////////////////
	/**
	 * Monitor a port for connections. Each time one is established, pass
	 * resulting Socket to handleConnection.
	 */

	public void listen() {
		try {
			ServerSocket listener = new ServerSocket(port);
			Socket server;

			System.out.println("Streznik se je zagnal....");
			while (true) {
				server = listener.accept();
				handleConnection(server);
				
			}

		} catch (IOException ioe) {
			System.out.println("IOException: " + ioe);
			ioe.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * V tej metodi je definirano, kaj streznik dela s sporocili odjemalca.
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
		String priimekOsebe = doc.getElementsByTagName("priimek").item(0).getTextContent();
		System.out.println("Ime osebe je: " + imeOsebe + " " +priimekOsebe);
		
		//uzame koordinati X in Y
		String X = doc.getElementsByTagName("X").item(0).getTextContent();
		String Y = doc.getElementsByTagName("Y").item(0).getTextContent();
		
		//v igri postavi križec
		igraView.zamenjajPlosco(Integer.valueOf(X), Integer.valueOf(Y), 2);
		
		String ODGOVOR = "premaknjeno";
		

		// ustvari XML sporocilo - za demo bo dovolj kar enakega kot prej
		String output = null;
		try {
			output = serializeDocumentToString(buildResponseDocument(ODGOVOR+ "\n Move made."));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.exit(1);
		}

		//poslji sporocilo in zapri povezavo
		out.writeUTF(output);
		
		//beri vrstico (samo da se ustavi program - da ahko pokažemo povezavo z netstat)
		//BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
	    //String s = bufferRead.readLine();
		System.out.println("Accepted\n");
	    server.close();
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

		Element root = doc.createElement("sporocilo");
		doc.appendChild(root);

		root.appendChild(doc.createTextNode(sporocilo));
		
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
