package greenseed.eurecom.fr.greenseed;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

/**
 * Created by Eva on 28/11/2015.
 */
public class OrganizationAdapter extends ArrayAdapter<Organization> {
    private Context mContext;
    private List<Organization> mOrganizations;

    public OrganizationAdapter(Context context, List<Organization> objects) {
        super(context, R.layout.organization_row_item, objects);
        this.mContext = context;
        this.mOrganizations = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
            convertView = mLayoutInflater.inflate(R.layout.organization_row_item, null);
        }

        Organization organization = mOrganizations.get(position);

        TextView descriptionView = (TextView) convertView.findViewById(R.id.organization_description);

        descriptionView.setText(organization.getName());

        ImageView imageView = (ImageView) convertView.findViewById(R.id.org_pic);
        ParseFile imageFile = organization.getImage();
        byte[] bitmapdata = new byte[0];
        try {
            bitmapdata = imageFile.getData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
        imageView.setImageBitmap(bitmap);

        ImageView imageViewInfo = (ImageView) convertView.findViewById(R.id.info_pic);
        imageViewInfo.setTag(position);

        return convertView;
    }

}
