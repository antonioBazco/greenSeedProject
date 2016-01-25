package greenseed.eurecom.fr.greenseed;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by Antonio on 28/11/2015.
 */
@ParseClassName("Payment")
public class Payment extends ParseObject{
    public Payment(){}

    public Payment(String value, String matter, Project project){

        put("value", Integer.parseInt(value));
        put("matter", matter);
        put("date", new Date());
        put("user", ParseUser.getCurrentUser());
        setACL(new ParseACL(ParseUser.getCurrentUser()));
        put("project", ParseObject.createWithoutData("Project", project.getObjectId()));
        put("organization", ParseObject.createWithoutData("Organization", project.getOrganizationID()));
    }

    public Integer getValue(){
        return getInt("value");
    }
    public String getMatter(){
        return getString("matter");
    }
    public Date getDate(){
        return getDate("date");
    }
    public Project getProject(){
        return (Project) getParseObject("project");
    }

}
