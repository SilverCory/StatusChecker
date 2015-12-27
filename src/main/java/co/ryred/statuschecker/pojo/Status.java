package co.ryred.statuschecker.pojo;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Cory Redmond on 27/12/2015.
 *
 * @author Cory Redmond <ace@ac3-servers.eu>
 */
public class Status {

    @Getter
    private HashMap<String, HashMap<String, String>> alive;

    @Getter
    private HashMap<String, ArrayList<String>> dead;

}
