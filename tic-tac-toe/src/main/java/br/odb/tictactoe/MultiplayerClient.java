package br.odb.tictactoe;

public interface MultiplayerClient {
	void receiveGameId( int gameId );
	void receivePlayerId( int playerId );
	void receiveTeamId( int teamId );
	void receiveMove( byte[] data );
	void endOfTurn();
	void allowedToSendMove();
	int getAppId();
	int getUserId();	
}
