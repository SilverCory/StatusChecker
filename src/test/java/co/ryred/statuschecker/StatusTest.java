package co.ryred.statuschecker;

import co.ryred.statuschecker.pojo.Status;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Cory Redmond on 27/12/2015.
 *
 * @author Cory Redmond <ace@ac3-servers.eu>
 */
public class StatusTest {

    @Test
    public void json_testing() throws Exception {
        StatusTask.urlString = "http://mc.hcserver.com/status/status?[random]";
        URL url = new URL( StatusTask.urlString.replace( "[random]", String.valueOf( StatusTask.random.nextInt() ) ) );

        String string = new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next();
        System.out.println( "Test JSON:" );
        System.out.println( string );
        System.out.println();

        Status status = StatusTask.gson.fromJson(string, Status.class);

        System.out.println( "Status: " );
        System.out.println( "  Dead: " );

        for( Map.Entry<String, ArrayList<String>> entry : status.getDead().entrySet() ) {
            System.out.println( "    " + entry.getKey() + ": " );
            for( String deadserver : entry.getValue() ) {
                System.out.println( "      " + deadserver + ": DEAD" );
            }
        }

        System.out.println( "  Alive: " );
        for( Map.Entry<String, HashMap<String, String>> entry : status.getAlive().entrySet() ) {
            System.out.println( "    " + entry.getKey() + ": " );
            for( Map.Entry<String, String> servers : entry.getValue().entrySet() ) {
                System.out.println( "      " + servers.getKey() + ": " + servers.getValue() );
            }
        }

    }

}
