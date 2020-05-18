package br.odb.multiplayer.model.rpg;

import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Direction;
import br.odb.gameworld.Kind;
import br.odb.gameworld.Location;
import br.odb.gameworld.Place;
import br.odb.gameworld.exceptions.InvalidLocationException;
import br.odb.multiplayer.model.Game;
import br.odb.multiplayer.model.Player;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class RPGGame extends Game {
	
	static Kind NPC = new Kind("NPC");
	final Map<String, CharacterActor> charactersByPlayerId = new HashMap<>();
	Place place = new Place();
	
    int turn = 0;

    public RPGGame(int i) {
        super(i, 2);
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("111111\n");
        sb.append("101001\n");
        sb.append("101001\n");
        sb.append("100011\n");
        sb.append("111111\n");
        
        Location entryHall = new Room2D("entry_hall", sb.toString());
        entryHall.setDescription("Entry hall");
        
        sb = new StringBuilder();
        sb.append("111111\n");
        sb.append("100001\n");
        sb.append("101101\n");
        sb.append("101001\n");
        sb.append("111111\n");
        
        Location middleHall = new Room2D("middle_hall", sb.toString());

        middleHall.setDescription("Middle hall");
        place.addLocation(middleHall);
        place.addLocation( entryHall );
        place.makeConnection(middleHall, Direction.N, entryHall);
    }

    @Override
    public void checkForGameEnd() {
    }

    @Override
    public void sendMove(HashMap<String, String> params) {
        
    	String cmd = params.get("cmd");
    	String playerId = params.get("playerId");
    	CharacterActor ac = charactersByPlayerId.get(playerId);
    	
    	if ("moveBy".contentEquals(cmd)) {
    		Location location = ac.getLocation();
    		Direction d = Direction.getDirectionForSimpleName(params.get("direction"));
    		Location destination = location.getConnections()[d.ordinal()];
    		if (destination != null ) {
    			location.removeCharacter(ac);
    			destination.addCharacter(ac);
    		}
    	} else if ("walkBy".contentEquals(cmd)) {
    		Character2D c2d = (Character2D)ac;
    		Direction d = Direction.getDirectionForSimpleName(params.get("direction"));
    		
    		if (c2d != null) {
    			
    			switch (d) {
    			case N:
    				c2d.position.y--;
    				break;
    			case E:
    				c2d.position.x++;
    				break;
    			case S:
    				c2d.position.y++;
    				break;
    			case W:
    				c2d.position.x--;
    				break;
    			}
    		}
    		    		
    	} else if ("say".contentEquals(cmd)) {
    		
    	}
    	
    	++turn;
        
    }

	public int addNewPlayer() {
		
		int id = super.addNewPlayer();
		
		CharacterActor ca = new Character2D("player" + id, NPC);
		
		charactersByPlayerId.put( "" + id, ca );
		
		try {
			place.getLocation("entry_hall").addCharacter(ca);
			return id;
		} catch (InvalidLocationException e) {
			e.printStackTrace();
			return -1;
		}
	}
    
    
    @Override
    public void writeState(String playerId, OutputStream os) {

        StringBuilder sb = new StringBuilder("<?xml version='1.0'?><game><state>");

        try {
            System.out.println("writing status for " + currentPlayerId);

                CharacterActor ca = charactersByPlayerId.get( playerId );
            	
            	Location location = ca.getLocation();
            	
                sb.append("</state><current>");
                sb.append(1);
                sb.append("</current><turn>");
                sb.append(turn);
                sb.append("</turn><location>");
                
                
                sb.append( ca.getLocation() );
                sb.append("</location></presence>");
                
                for ( CharacterActor c : location.getCharacters()) {
                	sb.append("- " + c.getName() + "\n");
                }
                
                sb.append("</presence></game>");

            os.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getNumberOfRequiredPlayers() {
        return 2;
    }

    @Override
    public int getNumberOfMaximumPlayers() {
        return 2;
    }
}
