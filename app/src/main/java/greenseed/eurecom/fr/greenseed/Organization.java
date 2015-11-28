package greenseed.eurecom.fr.greenseed;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Organization")
public class Organization extends ParseObject{
    public Organization(){

    }

    public String getName(){
        return getString("name");
    }


}