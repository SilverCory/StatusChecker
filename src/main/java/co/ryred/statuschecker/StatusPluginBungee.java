package co.ryred.statuschecker;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Cory Redmond on 27/12/2015.
 *
 * @author Cory Redmond <ace@ac3-servers.eu>
 */
public class StatusPluginBungee extends Plugin {

    @Override
    public void onEnable() {
        new BungeeConfig( this );
    }

}

final class BungeeConfig {

    private final Plugin plugin;
    private Configuration config;

    public BungeeConfig( Plugin plugin ) {

        this.plugin = plugin;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        if (!getFile().exists()) {
            try {
                getFile().createNewFile();
                try (InputStream is = getResourceAsStream(); OutputStream os = new FileOutputStream(getFile())) {
                    ByteStreams.copy(is, os);
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable to create config file", e);
            }
        }

        getConfig();

        TeamspeakTask.getConfig().setHost( getConfig().getString( "teamspeak.hostname", "localhost" ) );
        TeamspeakTask.getConfig().setQueryPort( getConfig().getInt("teamspeak.port", 10011));
        TeamspeakTask.setUsername( getConfig().getString( "teamspeak.username", "status" ) );
        TeamspeakTask.setPassword(getConfig().getString("teamspeak.password", "Password123"));
        TeamspeakTask.getAlertGroups().addAll( getConfig().getIntList("teamspeak.alertGroupIds") );

        StatusTask.urlString = getConfig().getString( "status.url", "http://localhost:8080/status?[random]" );

        StatusTask.everyAlerts = getConfig().getInt( "alerts.repeatingAlert.everyChances", 150 );
        StatusTask.everyMessage = getConfig().getString("alerts.repeatingAlert.message", "The server [server] on [category] has been down for a while now!!" );

        StatusTask.firstAlert = getConfig().getInt( "alerts.firstAlert.chances" );
        StatusTask.firstMessage = getConfig().getString("alerts.firstAlert.message", "The server [server] is down on [category]!" );

        int repeatTime = getConfig().getInt( "status.reapeatTime", 5 );

        StatusTask statusTask = new StatusTask(messages -> plugin.getProxy().getScheduler().runAsync(plugin, new TeamspeakTask(messages)));

        plugin.getProxy().getScheduler().schedule( plugin, statusTask, repeatTime, repeatTime, TimeUnit.SECONDS );

    }

    public Configuration getConfig()
    {
        try {
            return this.config == null ? ( this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load( getFile() ) ) : config;
        } catch ( IOException e ) {
            e.printStackTrace();
            return null;
        }
    }

    private File getFile()
    {
        return new File( plugin.getDataFolder(), "config.yml");
    }

    private InputStream getResourceAsStream()
    {
        return plugin.getResourceAsStream( "config.yml" );
    }

}