package greenseed.eurecom.fr.greenseed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class ProjectListActivity extends AppCompatActivity  implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private ProjectAdapter mProjectAdapter;
    public Project project;
    EditText orgSearch;
    private ParseObject payment;
    private List<Project> projectList;
    private int averagePayment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        ParseObject.registerSubclass(Organization.class);
        ParseObject.registerSubclass(Project.class);
        ParseObject.registerSubclass(Payment.class);

        mProjectAdapter = new ProjectAdapter(this, new ArrayList<Project>());

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
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        // download the projects
        projectList = Application.getList();
        averagePayment = Application.getAveragePayment();

        mProjectAdapter.clear();
        mProjectAdapter.addAll(projectList);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        project = mProjectAdapter.getItem(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.ThemeDialogCustom);

        builder.setTitle("Type quantity (€)");

        // Set up the input
        final EditText input = new EditText(this);
        input.setBackgroundColor(Color.parseColor("#B2EC5D"));//F4F9EE
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setText("" + String.valueOf(averagePayment));
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("DONATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //String m_Text = input.getText().toString();
                // Saving the payment in the database
                payment = new Payment(input.getText().toString(), project.getMatter(), project);

                //payment.setACL(new ParseACL(ParseUser.getCurrentUser()));
                payment.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            paymentRegisteredInDatabase();
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
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.paypal,
                (ViewGroup) findViewById(R.id.relativeLayout1));

        AlertDialog.Builder paypal = new AlertDialog.Builder(this);
        paypal.setView(view);
        paypal.setPositiveButton("Pay",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = getString(R.string.donation_confirmation);
                                String info = getString(R.string.sharing_question_1) +  payment.get("value") + getString(R.string.sharing_question_2);
                                openOptionsHelpDialog(name, info);
                            }
                        }
                );
        AlertDialog dialogPaypal = paypal.create();
        dialogPaypal.show();

        Button b = dialogPaypal.getButton(DialogInterface.BUTTON_POSITIVE);
        if(b != null)
            b.setTextColor(Color.parseColor("#66CC00"));

    }

    private void openOptionsHelpDialog(String name, String info)
    {
        new AlertDialog.Builder(this)
                .setTitle(name).setMessage(info)
                .setPositiveButton("Share",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                shareOnFacebook();
//                                Intent myIntent = new Intent(ProjectListActivity.this, ShareOnFacebook.class);
//                                startActivity(myIntent);
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

    private void shareOnFacebook () {
        String urlToShare = "https://www.facebook.com/dialog/share?app_id=145634995501895&display=popup&href=http://antoniobazco.github.io/";//&redirect_uri=https%3A%2F%2Fdevelopers.facebook.com%2Ftools%2Fexplorer";
        //https://www.facebook.com/dialog/feed?app_id=145634995501895&display=popup&caption=An%20example%20caption&link=http://antoniobazco.github.io/&redirect_uri=http://antoniobazco.github.io/

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        // intent.putExtra(Intent.EXTRA_SUBJECT, "Foo bar"); // NB: has no effect!
        intent.putExtra(Intent.EXTRA_TEXT, urlToShare);

        // See if official Facebook app is found
        boolean facebookAppFound = false;
        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("greenseed.eurecom.fr.greenseed")) {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;
            }
        }

        // As fallback, launch sharer.php in a browser
        if (!facebookAppFound) {
            String sharerUrl =urlToShare;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }

        startActivity(intent);
    }

    public void fastDonation() {

    }
}
