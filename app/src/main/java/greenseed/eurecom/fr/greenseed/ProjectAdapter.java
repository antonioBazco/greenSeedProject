package greenseed.eurecom.fr.greenseed;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

/**
 * Created by Eva on 28/11/2015.
 */
public class ProjectAdapter extends ArrayAdapter<Project> {
    private Context mContext;
    private List<Project> mProjects;
    private List<Project> mOriginalValues;

    public ProjectAdapter(Context context, List<Project> objects) {
        super(context, R.layout.project_row_item, objects);
        this.mContext = context;
        this.mProjects = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
            convertView = mLayoutInflater.inflate(R.layout.project_row_item, null);
        }

        Project project = mProjects.get(position);

        TextView descriptionView = (TextView) convertView.findViewById(R.id.project_description);

        descriptionView.setText(project.getName());

        return convertView;
    }


    @Override
    public int getCount() {
        return mProjects.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mProjects = (List<Project>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<Project> FilteredArrList = new ArrayList<Project>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<Project>(mProjects); // saves the original data in mOriginalValues
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
                        Project org = mOriginalValues.get(i);
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
