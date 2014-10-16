package br.odb.tictactoe;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.tictactoe.commands.NewGameCommand;
import br.odb.tictactoe.commands.PutCommand;
import br.odb.tictactoe.commands.QuitCommand;
import br.odb.tictactoe.commands.ReceiveCommand;
import br.odb.tictactoe.commands.StatusCommand;

public class TicTacToeMainApp extends ConsoleApplication implements MultiplayerClient {

	public static enum Piece{ 
		EMPTY("[#]"), 
		NOUGHTS("[O]"), 
		CROSSES("[X]");
		
		String representation;
		
		Piece( String representation ) {
			this.representation = representation;
		}
		
		@Override
		public String toString() {
			return representation;
		}
	}

	public Piece[][] board;
	public Piece localPlayerTeam = Piece.NOUGHTS;
	private ServerProxy serverProxy = new ServerProxy( "http://localhost:8080/multiplayer-server" );
	private int gameId;
	private int playerId;
	public Piece currentTeam;
	private Piece winnerTeam;
	
    public TicTacToeMainApp() {

    }

    public static void main(String[] args) {

        // This will probably never change.
        TicTacToeMainApp oldLady = (TicTacToeMainApp) new TicTacToeMainApp()
                .setAppName("Tic-Tac-Toe!")
                .setAuthorName("Daniel 'MontyOnTheRun' Monteiro")
                .setLicenseName("3-Clause BSD").setReleaseYear(2014);
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

        for (UserCommandLineAction cmd : new UserCommandLineAction[]{
            new QuitCommand(this), new NewGameCommand( this ), new ReceiveCommand(), new PutCommand(),
            new StatusCommand(this)}) {

            this.registerCommand(cmd);
        }

        return super.init();
    }
    
	public void newGame() {
		
		board = new Piece[ 3 ][];
		
		for ( int c = 0; c < 3; ++c ) {
			board[ c ] = new Piece[ 3 ];
			for ( int d = 0; d < 3; ++d ) {
				board[ c ][ d ] = Piece.EMPTY;
			}
		}
		
		
		requestGameFromServer();
	}
	
	public void requestGameFromServer() {
		serverProxy.startConnection( this );
	}

	@Override
	public void receiveGameId(int gameId) {
		this.gameId = gameId;
		getClient().printVerbose( "got game id: " + gameId );
	}

	@Override
	public void receivePlayerId(int playerId) {
		this.playerId = playerId;
		getClient().printVerbose( "got player id: " + playerId );		
	}

	@Override
	public void receiveTeamId(int teamId) {
		this.localPlayerTeam = Piece.values()[ teamId ];
		getClient().printVerbose( "got local player team: " + localPlayerTeam );
	}

	@Override
	public void receiveMove(byte[] data) {
		
		int i = 0;
		
		Piece[] teams = Piece.values();
		
		for ( int c = 0; c < 3; ++c ) {
			for ( int d = 0; d < 3; ++d ) {
				this.board[ c ][ d ] = teams[ (int) data[ i ] ];
				++i;
			}
		}
		
		this.currentTeam = teams[ data[ i ] ];
		this.winnerTeam = teams[ data[ ++i ] ];

		if ( this.winnerTeam != teams[ 0 ] ) {
			
			getClient().printNormal( "team " + winnerTeam + " wins the match" );
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
		serverProxy.requestUpdate( this );		
	}

	@Override
	public int getAppId() {
		return gameId;
	}

	public void sendRemoteMove(int x, int y) {
		serverProxy.sendData( x, y, this );
		
	}

	@Override
	public int getUserId() {
		return playerId;
	}
}
