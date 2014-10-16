/**
 * 
 */
package br.odb.tictactoe;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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

		return address + "/GetGameId";
	}

	private String getGameStatus(int gameId) {
		return address + "/GetGameStatus?gameId=" + gameId;
	}
	
	private String sendDataURL(int appId, int userId, int x, int y ) {
		String toReturn = address + "/SendMove?gameId=" + appId + "&playerId=" + userId;
		
		toReturn += "&x=" + x;
		toReturn += "&y=" + y;
		
		return toReturn;
	}
	

	public void startConnection(MultiplayerClient client) {
		try {
			
			int gameId;
			int playerId;
			int teamId;
			
			byte[] response = requestDirectly( getGameId() );
			
			gameId = ( int) response[ 0 ];
			client.receiveGameId(gameId);
			 
			playerId = ( int ) response[ 1 ];
			client.receivePlayerId(playerId);
			
			teamId = ( int ) response[ 2 ];
			client.receiveTeamId(teamId);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
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

	private byte[] requestDirectly(String url) throws MalformedURLException,
			IOException {

		System.out.println("url: " + url);

		URLConnection connection = new URL(url).openConnection();
		connection.setRequestProperty("Accept-Charset", "utf8");
		InputStream response = connection.getInputStream();

		return readFully(response );
	}

	public void requestUpdate(MultiplayerClient client) {
		try {
			client.receiveMove( requestDirectly( getGameStatus( client.getAppId() ) ) );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendData( int x, int y, MultiplayerClient client ) {
		try {
			request( sendDataURL( client.getAppId(), client.getUserId(), x, y ) );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
