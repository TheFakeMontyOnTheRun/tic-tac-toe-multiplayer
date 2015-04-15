/**
 * 
 */
package br.odb.spacedames;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.odb.spacedames.SpaceDamesMainApp.Piece;

/**
 * @author monty
 *
 */
public class ServerProxy {

	private String address;

	public ServerProxy(String address) {
		this.address = address;
	}

	public String getGameId() {

		return address + "/GetGameId?gameType=2";
	}

	private String getGameStatus(int gameId) {
		return address + "/GetGameStatus?gameId=" + gameId;
	}
	
	private String sendDataURL(int appId, int userId, int x0, int y0, int x1, int y1 ) {
		String toReturn = address + "/SendMove?gameId=" + appId + "&playerId=" + userId;
		
		toReturn += "&x0=" + x0;
		toReturn += "&y0=" + y0;

		toReturn += "&x1=" + x1;
		toReturn += "&y1=" + y1;

		
		return toReturn;
	}
	
	
	public Node getGameNodeFromURL( String url ) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse( url );
		doc.getDocumentElement().normalize();

		NodeList nodeLst;
		nodeLst = doc.getElementsByTagName("game");
		return nodeLst.item(0);
	}
	
	public Node getGameNode( InputStream is ) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse( is );
		doc.getDocumentElement().normalize();

		NodeList nodeLst;
		nodeLst = doc.getElementsByTagName("game");
		return nodeLst.item(0);
	}
	

	public void startConnection(MultiplayerClient client) {
		try {
			
			Node gameNode;
			gameNode = getGameNodeFromURL( getGameId() );

			NodeList nodeLst = gameNode.getChildNodes();

			for (int s = 0; s < nodeLst.getLength(); s++) {

				Node fstNode = nodeLst.item(s);

				if (fstNode != null) {

					if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

						if (fstNode.getNodeName().equals("gameId")) {
							client.receiveGameId( Integer.parseInt( fstNode.getTextContent() ) );
						} else if (fstNode.getNodeName().equals("playerId")) {
							client.receivePlayerId( Integer.parseInt( fstNode.getTextContent() ) );
						} else if (fstNode.getNodeName().equals("teamId")) {
							client.receiveTeamId( Integer.parseInt( fstNode.getTextContent() ) );
						}
					}
				}
			}			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

	}

	// http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
	public String readFully(InputStream inputStream, String encoding)
			throws IOException {
		return new String(readFully(inputStream), encoding);
	}

	private byte[] readFully(InputStream inputStream) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024];
		int length = 0;

		while ((length = inputStream.read(buffer)) != -1) {
			baos.write(buffer, 0, length);
		}

		return baos.toByteArray();
	}

	private String request(String url) throws MalformedURLException,
			IOException {

		System.out.println("url: " + url);

		URLConnection connection = new URL(url).openConnection();
		connection.setRequestProperty("Accept-Charset", "utf8");
		InputStream response = connection.getInputStream();

		return readFully(response, "utf8");
	}


	public void requestUpdate(MultiplayerClient client) {
		try {
			client.receiveMove( getGameNodeFromURL( getGameStatus( client.getAppId() ) ) );
		} catch ( Exception e) {
			e.printStackTrace();
		}
	}

	public void sendData( int x0, int y0, int x1, int y1, MultiplayerClient client ) {
		try {
			request( sendDataURL( client.getAppId(), client.getUserId(), x0, y0, x1, y1 ) );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
