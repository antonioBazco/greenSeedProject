package greenseed.eurecom.fr.greenseed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class DonationListActivity extends AppCompatActivity  implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private OrganizationAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_list);

        ParseObject.registerSubclass(Organization.class);

        mAdapter = new OrganizationAdapter(this, new ArrayList<Organization>());

        mListView = (ListView) findViewById(R.id.organization_list);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        updateData();
    }

    public void updateData(){
        ParseQuery<Organization> query = ParseQuery.getQuery(Organization.class);
        query.findInBackground(new FindCallback<Organization>() {

            @Override
            public void done(List<Organization> organizations, ParseException error) {
                if (organizations != null) {
                    mAdapter.clear();
                    mAdapter.addAll(organizations);
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Organization organization = mAdapter.getItem(position);
        String idOrg = organization.getObjectId();

        Intent intent = new Intent(DonationListActivity.this, ProjectListActivity.class);
        Bundle b = new Bundle();
        b.putString("key", idOrg); //Your id
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
        finish();
    }

    public void searchOrg() {
        Toast.makeText(DonationListActivity.this, "Not implemented yet", Toast.LENGTH_LONG).show();
    }
}
