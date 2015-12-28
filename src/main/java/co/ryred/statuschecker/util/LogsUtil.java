package co.ryred.statuschecker.util;

import lombok.Getter;
import lombok.Setter;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Cory Redmond
 *         Created by acech_000 on 22/05/2015.
 */
@SuppressWarnings("SameParameterValue")
public class LogsUtil
{

    @Setter
    private static Logger logger = Logger.getGlobal();

    @Getter
    @Setter
    private static boolean debug = true;

    public static Logger getLogger()
    {
        return logger == null ? ( logger = Logger.getGlobal() ) : logger;
    }

    public static boolean _D()
    {
        return isDebug();
    }

    public static void _D( Object... objects )
    {

        //if( !isDebug() ) return;

        StringBuilder sb = new StringBuilder( "[D]" );

        for ( Object obj : objects )
            sb.append( " | " ).append( obj );

        log( Level.INFO, sb );

    }

    public static void info( String string )
    {
        getLogger().info( string );
    }

    public static void fine( String string )
    {
        getLogger().fine( string );
    }

    public static void finer( String string )
    {
        getLogger().finer( string );
    }

    public static void finest( String string )
    {
        getLogger().finest( string );
    }

    public static void severe( String string )
    {
        getLogger().severe( string );
    }

    public static void warning( String string )
    {
        getLogger().warning( string );
    }

    public static void log( Level level, Object object )
    {
        getLogger().log( level, object.toString() );
    }

}