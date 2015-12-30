package co.ryred.statuschecker;

import co.ryred.statuschecker.util.LogsUtil;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created by Cory Redmond on 27/12/2015.
 *
 * @author Cory Redmond <ace@ac3-servers.eu>
 */
public class TeamspeakTask implements Runnable {

    @Getter
    private static TS3Config config = new TS3Config();

    @Setter
    @Getter
    private static ArrayList<Integer> alertGroups = new ArrayList<>();

    @Getter
    @Setter
    private static String username = "";

    @Getter
    @Setter
    private static String password = "";

    private final ArrayList<String> messages;

    public TeamspeakTask( ArrayList<String> messages ) {
        this.messages = messages;
    }

    @Override
    public void run() {

        config.setFloodRate(TS3Query.FloodRate.UNLIMITED);
        config.setDebugLevel(Level.WARNING);
        if(LogsUtil._D()) config.setDebugLevel(Level.INFO);
        config.setDebugToFile(false);

        final TS3Query query = new TS3Query(config);
        query.connect();

        TS3Api api = query.getApi();

        api.selectVirtualServerById(1);
        api.login( username, password );
        api.setNickname("ServerStatusBot");

        api.getClients().stream().filter(client -> client.getType() == 0 && hasGroup(client.getServerGroups())).forEach(client -> {
            for (String message : messages)
                api.pokeClient(client.getId(), message);
        });

        messages.forEach(api::sendServerMessage);

        query.exit();

    }

    private boolean hasGroup(int[] serverGroups) {
        for( int number : serverGroups ) {
            if( alertGroups.contains(number) ) return true;
        }
        return false;
    }

}
