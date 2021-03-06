package greenseed.eurecom.fr.greenseed;

/**
 * Application class to save global state.
 */

import android.content.Context;
import android.content.SharedPreferences;
import java.util.List;
import com.parse.Parse;
import com.parse.ParseObject;

public class Application extends android.app.Application {
    // Debugging switch
    public static final boolean APPDEBUG = false;

    // Debugging tag for the application
    public static final String APPTAG = "GrowthAid";

    private static SharedPreferences preferences;

    private static ConfigHelper configHelper;

    private static List<Project> savedProjects;

    private static List<Organization> savedOrgs;

    private static int averagePayment;

    private static Project breakingProject;


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

    public static List<Organization> getOrgList() {
        return savedOrgs;
    }

    public static void saveOrgList(List<Organization> Orgs) {
        savedOrgs = Orgs;
    }

    public static List<Project> getList() {
        return savedProjects;
    }

    public static void saveList(List<Project> projects) {
        savedProjects = projects;
    }

    public static void saveAveragePayment(int averagePaymentValue) {
        averagePayment = averagePaymentValue;
    }

    public static int getAveragePayment(){
        return averagePayment;
    }

    public static void saveBreakingProject(Project breakingProjectIn) {
        breakingProject = breakingProjectIn;
    }

    public static Project getBreakingProject(){
        return breakingProject;
    }



}
