package greenseed.eurecom.fr.greenseed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DonationListActivity extends AppCompatActivity  implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private OrganizationAdapter mAdapter;
    EditText orgSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_list);

        ParseObject.registerSubclass(Organization.class);

        mAdapter = new OrganizationAdapter(this, new ArrayList<Organization>());

        mListView = (ListView) findViewById(R.id.organization_list);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setTextFilterEnabled(true);

        orgSearch = (EditText) findViewById(R.id.organization_input);
        orgSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                DonationListActivity.this.mAdapter.getFilter().filter(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
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
        //finish();
    }

    public void showOrgInfo(View view) {
        Object position = view.getTag();
        System.out.println(position);
        Organization organization = mAdapter.getItem((int) position);
        String name = organization.getName();
        ParseFile infoOrg = organization.getInfo();
        byte[] infoData = new byte[0];
        try {
            infoData = infoOrg.getData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String info = new String(infoData);
        openOptionsHelpDialog(name, info);
    }

    private void openOptionsHelpDialog(String name, String info)
    {
        new AlertDialog.Builder(this)
                .setTitle(name).setMessage(info)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        }
                )
                .show();
    }
}
