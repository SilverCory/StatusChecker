package co.ryred.statuschecker;

import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Cory Redmond on 27/12/2015.
 *
 * @author Cory Redmond <ace@ac3-servers.eu>
 */
public class Main {

    public static void main( String... args ) throws IOException {

        ArrayList<Integer> ints = new ArrayList<>();
        ints.add(6);

        TeamspeakTask.setAlertGroups( ints );
        TeamspeakTask.getConfig().setHost("ts.hcserver.com");

        TeamspeakTask.setUsername("status");
        TeamspeakTask.setPassword("QMlPCTEP");

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("test_1");
        arrayList.add("test_2");

        TeamspeakTask tst = new TeamspeakTask( arrayList );

        BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
        String line;

        while( (line = br.readLine()) != null ) {
            boolean stop = false;
            switch (line.toUpperCase()) {
                case "RUN":
                    tst.run();
                    break;
                case "STOP":
                    stop = true;
                default:
                    System.out.println( "Unknown command!" );
                    break;
            }
            if( stop ) break;
        }

    }

}
