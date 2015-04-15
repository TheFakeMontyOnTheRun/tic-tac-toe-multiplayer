package br.odb.spacedames;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.spacedames.commands.NewGameCommand;
import br.odb.spacedames.commands.PutCommand;
import br.odb.spacedames.commands.QuitCommand;
import br.odb.spacedames.commands.ReceiveCommand;
import br.odb.spacedames.commands.StatusCommand;

public class SpaceDamesMainApp extends ConsoleApplication implements
		MultiplayerClient {

	public static final int BOARD_SIZE = 8;

	public static enum Piece {
		EMPTY("[#]"), NOUGHTS("[1]"), CROSSES("[2]");

		String representation;

		Piece(String representation) {
			this.representation = representation;
		}

		@Override
		public String toString() {
			return representation;
		}
	}

	public Piece[][] board;
	public Piece localPlayerTeam = Piece.NOUGHTS;
	private ServerProxy serverProxy = new ServerProxy(
			"http://localhost:8080/multiplayer-server");
	private int gameId;
	private int playerId;
	public Piece currentTeam;
	private Piece winnerTeam;

	public SpaceDamesMainApp() {

	}

	public static void main(String[] args) {

		// This will probably never change.
		SpaceDamesMainApp oldLady = (SpaceDamesMainApp) new SpaceDamesMainApp()
				.setAppName("Space Dames")
				.setAuthorName("Daniel 'MontyOnTheRun' Monteiro")
				.setLicenseName("3-Clause BSD").setReleaseYear(2015);
		oldLady.createDefaultClient();
		oldLady.start();
	}

	@Override
	public void log(String tag, String message) {
		getClient().printVerbose(tag + ":" + message);
	}

	@Override
	public void onDataEntered(String entry) {

		if (entry == null || entry.length() == 0) {
			return;
		}

		super.onDataEntered(entry);

		try {

			runCmd(entry);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@Override
	protected void doQuit() {
		this.continueRunning = false;
	}

	@Override
	public ConsoleApplication init() {
		continueRunning = true;

		for (UserCommandLineAction cmd : new UserCommandLineAction[] {
				new QuitCommand(this), new NewGameCommand(this),
				new ReceiveCommand(), new PutCommand(), new StatusCommand(this) }) {

			this.registerCommand(cmd);
		}

		return super.init();
	}

	public void newGame() {

		board = new Piece[ BOARD_SIZE ][];

		for (int c = 0; c < BOARD_SIZE; ++c) {
			board[c] = new Piece[BOARD_SIZE];
			for (int d = 0; d < BOARD_SIZE; ++d) {
				board[c][d] = Piece.EMPTY;
			}
		}

		requestGameFromServer();
	}

	public void requestGameFromServer() {
		serverProxy.startConnection(this);
	}

	@Override
	public void receiveGameId(int gameId) {
		this.gameId = gameId;
		getClient().printVerbose("got game id: " + gameId);
	}

	@Override
	public void receivePlayerId(int playerId) {
		this.playerId = playerId;
		getClient().printVerbose("got player id: " + playerId);
	}

	@Override
	public void receiveTeamId(int teamId) {
		this.localPlayerTeam = Piece.values()[teamId];
		getClient().printVerbose("got local player team: " + localPlayerTeam);
	}

	@Override
	public void receiveMove(Node gameNode) {

		Piece[] teams = Piece.values();

		int i = 0;

		NodeList nodeLst = gameNode.getChildNodes();

		for (int s = 0; s < nodeLst.getLength(); s++) {

			Node fstNode = nodeLst.item(s);

			if (fstNode != null) {

				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

					if (fstNode.getNodeName().equals("state")) {

						String statusData = fstNode.getTextContent();

						for (int c = 0; c < BOARD_SIZE; ++c) {
							for (int d = 0; d < BOARD_SIZE; ++d) {
								this.board[c][d] = teams[statusData.charAt(i) - '0'];
								++i;
							}
						}

					} else if (fstNode.getNodeName().equals("current")) {
						this.currentTeam = teams[Integer.parseInt(fstNode
								.getTextContent())];
					} else if (fstNode.getNodeName().equals("winner")) {
						this.winnerTeam = teams[Integer.parseInt(fstNode
								.getTextContent())];
					}
				}
			}
		}

		if (this.winnerTeam != teams[0]) {

			getClient().printNormal("team " + winnerTeam + " wins the match");
		}
	}

	@Override
	public void endOfTurn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void allowedToSendMove() {
		// TODO Auto-generated method stub

	}

	public void receiveStatus() {
		serverProxy.requestUpdate(this);
	}

	@Override
	public int getAppId() {
		return gameId;
	}

	public void sendRemoteMove(int x0, int y0, int x1, int y1) {
		serverProxy.sendData(x0, y0, x1, y1, this);

	}

	@Override
	public int getUserId() {
		return playerId;
	}
}
