package greenseed.eurecom.fr.greenseed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectListActivity extends AppCompatActivity  implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private ProjectAdapter mProjectAdapter;
    public Project project;
    EditText orgSearch;
    private ParseObject payment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        ParseObject.registerSubclass(Organization.class);
        ParseObject.registerSubclass(Project.class);
        ParseObject.registerSubclass(Payments.class);

        mProjectAdapter = new ProjectAdapter(this, new ArrayList<Project>());

        Bundle b = getIntent().getExtras();
        String idOrg = b.getString("key");
        System.out.println(idOrg);

        mListView = (ListView) findViewById(R.id.project_list);
        mListView.setAdapter(mProjectAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setTextFilterEnabled(true);

        orgSearch = (EditText) findViewById(R.id.project_input);
        orgSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                ProjectListActivity.this.mProjectAdapter.getFilter().filter(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            @Override
            public void afterTextChanged(Editable arg0) {}
        });

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

        project = mProjectAdapter.getItem(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Type quantity (€)");

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
                // Saving the payment in the database
                payment = new ParseObject("Payment");

                payment.put("value", Integer.parseInt(input.getText().toString()));
                payment.put("matter", project.get("matter"));
                payment.put("date", new Date());
                payment.put("project", ParseObject.createWithoutData("Project", project.getObjectId()));

                payment.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            ParseUser user = ParseUser.getCurrentUser();
                            // Saving the payment in the user profile in database
                            List userPayments = user.getList("payments");
                            if (userPayments == null) {
                                userPayments = new ArrayList();
                            }
                            userPayments.add(ParseObject.createWithoutData("payments", payment.getObjectId()));
                            user.put("payments",userPayments);

                            user.saveInBackground(new SaveCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        paymentRegisteredInDatabase();
                                    } else {
                                        Toast.makeText(ProjectListActivity.this, getString(R.string.payment_error), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(ProjectListActivity.this, getString(R.string.payment_error), Toast.LENGTH_LONG).show();
                        }
                    }
                });
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

    public void paymentRegisteredInDatabase() {

        String name = getString(R.string.donation_confirmation);

        String info = getString(R.string.sharing_question_1) +  payment.get("value") + getString(R.string.sharing_question_2);

        openOptionsHelpDialog(name, info);
    }

    private void openOptionsHelpDialog(String name, String info)
    {
        new AlertDialog.Builder(this)
                .setTitle(name).setMessage(info)
                .setPositiveButton("Share",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent myIntent = new Intent(ProjectListActivity.this, ShareOnFacebook.class);
                                startActivity(myIntent);
                            }
                        }
                )
                .setNegativeButton("Not share",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(ProjectListActivity.this, "Thank you for your donation", Toast.LENGTH_LONG).show();
                                Intent myIntent = new Intent(ProjectListActivity.this, AnimActivity.class);
                                myIntent.putExtra("addSeed", "addOneSeed");
                                startActivity(myIntent);
                            }
                        }
                )
                .show();
    }
}
