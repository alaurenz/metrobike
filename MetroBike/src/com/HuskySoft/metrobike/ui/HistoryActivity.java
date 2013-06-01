package com.HuskySoft.metrobike.ui;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.HuskySoft.metrobike.R;
import com.HuskySoft.metrobike.ui.utility.History;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * This class shows the list of history addresses.
 * 
 * @author Sam Wilson
 * 
 */
public class HistoryActivity extends Activity {

    // This class pass the check style

    /**
     * The tag of this class.
     */
    private static final String TAG = "HistoryActivity";

    /**
     * The UI list view.
     */
    private ListView historyListView;

    /**
     * The adapter to store the addresses.
     */
    private ArrayAdapter<String> adapter;

    /**
     * The history object that contains all the addresses.
     */
    private History history;

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        history = History.getInstance();
        initializeHistoryListView();
        setListener();
        Log.v(TAG, "Done creating the history page");
    }

    /**
     * Set the list view listener.<br>
     * Hidden feature: user can delete single history by long click.
     */
    private void setListener() {
        historyListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, final View view,
                    final int position, final long id) {
                // get the string item
                final String item = (String) parent.getItemAtPosition(position);
                // builder an alertdialog
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(Html.fromHtml("<font color='red'>Warning</font>"));
                builder.setMessage("Are you sure want to delete " + item + "?");
                // Yes button
                builder.setPositiveButton("Yes", new OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        history.deleteAddress(item);
                        adapter.notifyDataSetChanged();
                        if (history.getSize() == 0) {
                            // nothing to show, go back to the setting activity
                            int emptyHistory = R.string.empty_history;
                            Context context = view.getContext();
                            Toast.makeText(context, emptyHistory, Toast.LENGTH_SHORT).show();
                            // kill this activity and go back to the previous
                            // one
                            Log.d(TAG, "No more addresses, quite this history page");
                            finish();
                        }
                    }
                });
                // No button
                builder.setNegativeButton("No", new OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        // close this dialog
                        dialog.cancel();
                    }
                });
                builder.create().show();
                return true;
            }
        });
    }

    /**
     * initialize the history and list view object.
     */
    private void initializeHistoryListView() {
        historyListView = (ListView) findViewById(R.id.history_listView);
        // get the list array from
        final List<String> list = history.getHistory();
        int layout = android.R.layout.simple_dropdown_item_1line;
        adapter = new ArrayAdapter<String>(this, layout, list);
        historyListView.setAdapter(adapter);

    }

    /**
     * We need to kill this activity if its not on the main screen.
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onPause()
     */
    @Override
    protected final void onPause() {
        super.onPause();
        Log.v(TAG, "Kill this history page");
        finish();
    }

    /**
     * We need to kill this activity if its not on the main screen.
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public final void onBackPressed() {
        super.onBackPressed();
        Log.v(TAG, "Kill this history page");
        finish();
    }

    /**
     * We need to kill this activity if its not on the main screen.
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected final void onDestroy() {
        super.onDestroy();
        saveHistoryFile();
        Log.v(TAG, "Destroy this history page");
    }

    /**
     * Write all histories into file.
     */
    private void saveHistoryFile() {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(History.FILENAME, Context.MODE_PRIVATE);
            final List<String> list = history.getHistory();
            for (String str : list) {
                Log.d(TAG, "Read list " + str);
                History.writeOneAddressToFile(fos, str);
            }
        } catch (FileNotFoundException e) {
            Log.i(TAG, "Cannot create history file");
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                Log.i(TAG, "Connot close the file ouput stream");
            }
        }
    }
}
