package greenseed.eurecom.fr.greenseed;

/**
 * Application class to save global state.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.parse.Parse;
import com.parse.ParseObject;

public class Application extends android.app.Application {
    // Debugging switch
    public static final boolean APPDEBUG = false;

    // Debugging tag for the application
    public static final String APPTAG = "GreenSeed";

    private static SharedPreferences preferences;

    private static ConfigHelper configHelper;

    public Application() {}

    @Override
    public void onCreate() {
        super.onCreate();

        // Initilize Parse API -data base-.
        Parse.initialize(this, "yEfX5bNStTL4C2kV7DvaeHJzgMQx51qXwur8j9lA",
                "Tm3duxC5izbfgIeupHzQhTyfXVuge65jlgYsXEXF");

        preferences = getSharedPreferences("greenseed.eurecom.fr.greenseed", Context.MODE_PRIVATE);

        configHelper = new ConfigHelper();
        configHelper.fetchConfigIfNeeded();
    }

    public static ConfigHelper getConfigHelper() {
        return configHelper;
    }
}
