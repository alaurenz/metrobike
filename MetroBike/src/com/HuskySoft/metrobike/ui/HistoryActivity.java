package com.HuskySoft.metrobike.ui;

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
    }

    /**
     * Set the list view listener.<br>
     * Hidden feature: user can delete single history by long click.
     */
    private void setListener() {
        historyListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, 
                    final View view, final int position, final long id) {
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

}
