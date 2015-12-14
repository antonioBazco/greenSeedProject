package greenseed.eurecom.fr.greenseed;

import android.graphics.Bitmap;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Organization")
public class Organization extends ParseObject {

    public Organization() {

    }

    public String getName() {
        return getString("name");
    }

    public ParseFile getImage() {
        return getParseFile("picture");
    }

    public ParseFile getInfo() {
        return getParseFile("infoAbout");
    }
}