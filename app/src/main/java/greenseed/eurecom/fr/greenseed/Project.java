package greenseed.eurecom.fr.greenseed;

import com.parse.ParseClassName;
import com.parse.ParseObject;
/**
 * Created by Eva on 28/11/2015.
 */
@ParseClassName("Project")
public class Project extends ParseObject{
    public Project(){

    }

    public String getName(){
        return getString("projectName");
    }
}
