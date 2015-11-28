package greenseed.eurecom.fr.greenseed;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Eva on 28/11/2015.
 */
public class ProjectAdapter extends ArrayAdapter<Project> {
    private Context mContext;
    private List<Project> mProjects;

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
}
