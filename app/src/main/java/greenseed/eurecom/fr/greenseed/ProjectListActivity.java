package greenseed.eurecom.fr.greenseed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ProjectListActivity extends AppCompatActivity  implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private ProjectAdapter mProjectAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        ParseObject.registerSubclass(Organization.class);
        ParseObject.registerSubclass(Project.class);

        mProjectAdapter = new ProjectAdapter(this, new ArrayList<Project>());

        Bundle b = getIntent().getExtras();
        String idOrg = b.getString("key");
        System.out.println(idOrg);

        mListView = (ListView) findViewById(R.id.project_list);
        mListView.setAdapter(mProjectAdapter);
        mListView.setOnItemClickListener(this);


        ParseQuery innerQuery = new ParseQuery("Organization");
        innerQuery.whereEqualTo("objectId", idOrg);

        ParseQuery query = new ParseQuery("Project");
        query.whereMatchesQuery("organization", innerQuery);
        query.findInBackground(new FindCallback<Project>() {
            @Override
            public void done(List<Project> projects, ParseException e) {
                if (projects != null) {
                    mListView.setAdapter(mProjectAdapter);
                    mProjectAdapter.clear();
                    mProjectAdapter.addAll(projects);
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Type quantity");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("DONATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //String m_Text = input.getText().toString();
                


                Toast.makeText(ProjectListActivity.this, "Thank you for your donation", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ProjectListActivity.this, MainActivity.class));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }

        );

        builder.show();

    }

    public void searchPro() {
        Toast.makeText(ProjectListActivity.this, "Not implemented yet", Toast.LENGTH_LONG).show();
    }
}
