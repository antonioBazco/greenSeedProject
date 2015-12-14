package greenseed.eurecom.fr.greenseed;

import com.parse.ParseClassName;
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

    public ParseObject getOrganization(){
        return getParseObject("organization");
    }

    public ParseFile getImage() {
        return getParseFile("picture");
    }

    public ParseFile getInfo() {
        return getParseFile("infoAbout");
    }
}
