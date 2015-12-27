package co.ryred.statuschecker;

import co.ryred.statuschecker.pojo.Status;
import com.google.gson.Gson;

import java.net.URL;
import java.util.*;

/**
 * Created by Cory Redmond on 27/12/2015.
 *
 * @author Cory Redmond <ace@ac3-servers.eu>
 */
public class StatusTask implements Runnable {

    public static Random random = new Random();
    public static String urlString = "http://localhost/status/status?[random]";
    public static final Gson gson = new Gson();

    public static int firstAlert = 5;
    public static String firstMessage = "The server [server] is down on [category]!";

    public static int everyAlerts = 150;
    public static String everyMessage = "The server [server] on [category] has been down for a while now!!";
    public static boolean bungeeOnly;

    private Status last_status = null;
    private HashMap<String, Integer> chances = new HashMap<>();

    private final AlertCallback callback;

    public StatusTask( AlertCallback callback ) {
        this.callback = callback;
    }

    @Override
    public void run() {

        ArrayList<String> messages = new ArrayList<>();

        try {
            URL url = new URL( urlString.replace( "[random]", String.valueOf( random.nextInt() ) ) );

            Status status = gson.fromJson( new Scanner( url.openStream(), "UTF-8" ).useDelimiter( "\\A" ).next(), Status.class);

            if( last_status != null ) {

                Iterator<Map.Entry<String, Integer>> deadIterator = chances.entrySet().iterator();

                while( deadIterator.hasNext() ) {
                    Map.Entry<String, Integer> entry = deadIterator.next();
                    String[] path = entry.getKey().split( "_-_-_" );

                    if( path.length == 2 && status.getDead().containsKey( path[0] ) ) {
                        ArrayList<String> deadList = status.getDead().get(path[0]);
                        if( doBungeeCheck( path[2] ) && deadList != null && deadList.contains( path[ 2 ] ) ) {
                            entry.setValue( entry.getValue() + 1 );
                        } else {
                            deadIterator.remove();
                        }
                    } else {
                        deadIterator.remove();
                    }

                    if( entry.getValue() == firstAlert ) {
                        messages.add(firstMessage.replace("[server]", path[1]).replace("[category]", path[0]));
                    }

                    if( (entry.getValue() % everyAlerts) == 0 ) {
                        messages.add(everyMessage.replace("[server]", path[1]).replace("[category]", path[0]));
                    }

                }

                for( Map.Entry<String, HashMap<String, String>> category : last_status.getAlive().entrySet() ) {

                    category.getValue().entrySet().stream().filter(entry -> status.getDead().containsKey(category.getKey())).forEach(entry -> {
                        ArrayList<String> kek = status.getDead().get(category.getKey());
                        if ( doBungeeCheck(entry.getKey()) && kek != null && kek.contains(entry.getKey())) {
                            chances.put(category.getKey() + "_-_-_" + entry.getKey(), 1);
                        }
                    });

                }

            }

            last_status = status;

            if( messages.size() > 0 ) callback.doAlert(messages);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean doBungeeCheck(String s) {
        return bungeeOnly && s.toUpperCase().contains("BUNGEE");
    }

}
