package co.ryred.statuschecker.plugins;

import co.ryred.statuschecker.StatusTask;
import co.ryred.statuschecker.TeamspeakTask;
import co.ryred.statuschecker.util.LogsUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Created by Cory Redmond on 27/12/2015.
 * @author Cory Redmond <ace@ac3-servers.eu>
 */
public class StatusPluginBukkit extends JavaPlugin {

    @Override
    public void onEnable() {

        if(!new File( getDataFolder(), "config.yml" ).exists())
            saveDefaultConfig();

        TeamspeakTask.getConfig().setHost( getConfig().getString( "teamspeak.hostname", "localhost" ) );
        TeamspeakTask.getConfig().setQueryPort( getConfig().getInt("teamspeak.port", 10011));
        TeamspeakTask.setUsername( getConfig().getString( "teamspeak.username", "status" ) );
        TeamspeakTask.setPassword(getConfig().getString("teamspeak.password", "Password123"));
        TeamspeakTask.getAlertGroups().addAll( getConfig().getIntegerList( "teamspeak.alertGroupIds" ) );

        StatusTask.urlString = getConfig().getString( "status.url", "http://localhost:8080/status?[random]" );

        StatusTask.everyAlerts = getConfig().getInt( "alerts.repeatingAlert.everyChances", 150 );
        StatusTask.everyMessage = getConfig().getString("alerts.repeatingAlert.message", "The server [server] on [category] has been down for a while now!!" );

        StatusTask.firstAlert = getConfig().getInt( "alerts.firstAlert.chances" );
        StatusTask.firstMessage = getConfig().getString("alerts.firstAlert.message", "The server [server] is down on [category]!" );

        StatusTask.bungeeOnly = getConfig().getBoolean( "bungeeonly", true );

        LogsUtil.setDebug( getConfig().getBoolean( "debug", false ) );

        int repeatTime = getConfig().getInt( "status.reapeatTime", 5 ) * 20;

        StatusTask statusTask = new StatusTask(messages -> getServer().getScheduler().runTaskAsynchronously( StatusPluginBukkit.this, new TeamspeakTask( messages ) ));

        getServer().getScheduler().runTaskTimerAsynchronously( this, statusTask,repeatTime, repeatTime );

    }

}
