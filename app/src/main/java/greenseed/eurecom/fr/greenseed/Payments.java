package greenseed.eurecom.fr.greenseed;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by Antonio on 28/11/2015.
 */
@ParseClassName("Payments")
public class Payments extends ParseObject{
    public Payments(){

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
    public ParseObject getProject(){
        return getParseObject("project");
    }

}
