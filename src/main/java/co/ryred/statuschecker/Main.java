package co.ryred.statuschecker;

import co.ryred.statuschecker.util.LogsUtil;
import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by Cory Redmond on 27/12/2015.
 *
 * @author Cory Redmond <ace@ac3-servers.eu>
 */
public class Main {

    private static boolean running = true;

    public static void main( String... args ) throws IOException {

        LogsUtil.setLogger(Logger.getLogger("StatusChecker"));
        LogsUtil.setDebug( true );

        ArrayList<Integer> ints = new ArrayList<>();
        ints.add(6);

        StatusTask.urlString = "http://mc.hcserver.com/status/status?[random]";

        TeamspeakTask.setAlertGroups( ints );
        TeamspeakTask.getConfig().setHost("ts.hcserver.com");

        TeamspeakTask.setUsername("status");
        TeamspeakTask.setPassword("");

        StatusTask tst = new StatusTask(messages -> new Thread( new TeamspeakTask( messages ) ).start());

        new Thread( "kek" ) {
            @Override
            public void run() {

                while( running ) {

                    try {
                        tst.run();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    try {
                        Thread.sleep(5000L);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        }.start();

        BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );
        String line;
        while ( (line = reader.readLine()) != null ) {

            switch ( line.toLowerCase().split( " " )[0] ) {

                case "stop":
                    running = false;
                    break;
                default:
                    System.out.println( "Unknown command!" );
                    break;
            }

            if( !running ) break;

        }

    }

}
