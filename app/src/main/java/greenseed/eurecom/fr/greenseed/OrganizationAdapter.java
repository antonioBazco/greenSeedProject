package greenseed.eurecom.fr.greenseed;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

/**
 * Created by Eva on 28/11/2015.
 */
public class OrganizationAdapter extends ArrayAdapter<Organization> implements Filterable {
    private Context mContext;
    private List<Organization> mOrganizations;
    private List<Organization> mOriginalValues;

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

    @Override
    public int getCount() {
        return mOrganizations.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mOrganizations = (List<Organization>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<Organization> FilteredArrList = new ArrayList<Organization>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<Organization>(mOrganizations); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        Organization org = mOriginalValues.get(i);
                        String data = org.getName();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(org);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}
