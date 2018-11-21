package com.marakana.android.yamba;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;

public class TimelineFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = TimelineFragment.class.getSimpleName();
    private static final String[] FROM = {StatusContract.Column.USER,
            StatusContract.Column.MESSAGE,StatusContract.Column.CREATED_AT,
            StatusContract.Column.CREATED_AT};
    private static final int[] TO = {R.id.list_item_text_user,
            R.id.list_item_text_message, R.id.list_item_text_created_at};//,
    //R.id.list_item_freshness};
    private static final int LOADER_ID = 42;
    private SimpleCursorAdapter adapter;
    private static final ViewBinder VIEW_BINDER = new ViewBinder(){
        @Override
        public boolean setViewValue(View view, Cursor cursor,
                                    int columnIndex) {
            long timestamp;
// Custom binding
            switch (view.getId()) {

                case R.id.list_item_text_created_at:
                    timestamp = cursor.getLong(columnIndex);
                    CharSequence relTime = DateUtils
                            .getRelativeTimeSpanString(timestamp);
                    ((TextView) view).setText(relTime);
                    return true;
                //case R.id.list_item_freshness:
                //timestamp = cursor.getLong(columnIndex);
                //((FreshnessView) view).setTimestamp(timestamp);
                //return true;
                default:
                    return false;
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        setEmptyText("Loading data ...");

        adapter = new SimpleCursorAdapter(getActivity(),R.layout.list_item,null,
                FROM, TO , CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        adapter.setViewBinder(VIEW_BINDER);
        setListAdapter(adapter);
        getLoaderManager().initLoader(LOADER_ID,null,this);
    }

    public void onListItemClick(ListView l,View v, int position, long id){
        //get the details fragment
        DetailsFragment fragment = (DetailsFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_details);

        //is details fragment visible?
        if (fragment != null && fragment.isVisible()){
            fragment.updateView(id);
        }else {
            startActivity(new Intent(getActivity(),DetailsActivity.class)
                    .putExtra(StatusContract.Column.ID,id));
        }
    }

//loader callbacks
    //executed on non-ui thread
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id!=LOADER_ID)
            return null;
        Log.d(TAG,"onCreateLoader");

        return new CursorLoader(getActivity(),StatusContract.CONTENT_URI,
                null,null,null,StatusContract.DEFAULT_SORT);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //get the details fragment
        DetailsFragment fragment = (DetailsFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_details);

        //is details fragment visible?
        if (fragment!=null && fragment.isVisible()&&cursor.getCount()==0){
            fragment.updateView(-1);
            Toast.makeText(getActivity(),"No data",Toast.LENGTH_LONG).show();
        }
        Log.d(TAG,"onloadFinished with cursor: " + cursor.getCount());
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }



    //Handles custom binding of data to view
    class TimelineViewBinder implements ViewBinder{
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex){
            if (view.getId()!=R.id.list_item_text_created_at)
                return false;
            //Converting timestamp to relative time
            long timestamp = cursor.getLong(columnIndex);
            CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(timestamp);
            ((TextView) view).setText(relativeTime);

            return true;

        }
    }
}
