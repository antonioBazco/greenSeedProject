package greenseed.eurecom.fr.greenseed;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
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
    public String getMatter(){
        return getString("matter");
    }

    public Organization getOrganization(){
        return (Organization) getParseObject("organization");
    }

    public String getOrganizationID(){
        return getParseObject("organization").getObjectId();
    }

    public ParseFile getImage() {
        return getParseFile("picture");
    }

    public ParseFile getInfo() {
        return getParseFile("infoAbout");
    }
}
