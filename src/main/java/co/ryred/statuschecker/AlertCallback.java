package co.ryred.statuschecker;

import java.util.ArrayList;

/**
 * Created by Cory Redmond on 27/12/2015.
 *
 * @author Cory Redmond <ace@ac3-servers.eu>
 */
public interface AlertCallback {

    void doAlert( ArrayList<String> messages );

}
