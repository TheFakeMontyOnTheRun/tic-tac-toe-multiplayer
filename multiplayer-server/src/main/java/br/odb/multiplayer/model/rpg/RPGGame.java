package br.odb.multiplayer.model.rpg;

import br.odb.multiplayer.model.Game;
import br.odb.multiplayer.model.Player;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class RPGGame extends Game {

    int turn = 0;

    public RPGGame(int i) {
        super(i, 2);
    }

    @Override
    public void checkForGameEnd() {
    }

    @Override
    public void sendMove(HashMap<String, String> params) {
        ++turn;
    }

    @Override
    public void writeState(OutputStream os) {

        StringBuilder sb = new StringBuilder("<?xml version='1.0'?><game><state>");

        try {
            System.out.println("writing status for " + currentPlayerId);

            Player p = players.get(currentPlayerId);

            if (p != null) {
                sb.append("</state><current>");
                sb.append(p.playerId);
                sb.append("</current><turn>");
                sb.append(turn);
                sb.append("</turn></game>");
            } else {
                System.out.println("current player is null!");
            }

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
