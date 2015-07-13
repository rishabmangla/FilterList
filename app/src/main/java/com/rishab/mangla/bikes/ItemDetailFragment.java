package com.rishab.mangla.bikes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rishab.mangla.bikes.dummy.DummyContent;

/**
 * A fragment representing a single Item detail screen.
 * It contains selctable items for applying filters
 */
public class ItemDetailFragment extends ListFragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
//    public static final String ARG_SORT_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private String mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int resId = android.R.layout.simple_list_item_multiple_choice;

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            setListAdapter(new ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.simple_list_item_multiple_choice,
                    android.R.id.text1,
                    getArguments().getStringArrayList(ARG_ITEM_ID)));
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments().containsKey(ARG_ITEM_ID)
                && getArguments().getStringArrayList(ARG_ITEM_ID).get(0).equalsIgnoreCase("price descending"))
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        else
            getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        setRetainInstance(true);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i("rishab","onListItemClick " + position);
        Log.i("rishab","checked count " + getListView().getCheckedItemCount());
        super.onListItemClick(l, v, position, id);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("rishab","onOptionsItemSelected " + item.getItemId());
        Log.i("rishab","checked count " + getListView().getCheckedItemCount());
        return super.onOptionsItemSelected(item);
    }

    public SparseBooleanArray getCheckedItems(){
        SparseBooleanArray chekcedPos = getListView().getCheckedItemPositions();
        return chekcedPos;
    }

    //    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);
//
//        // Show the dummy content as text in a TextView.
//        if (mItem != null) {
//            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem);
//        }
//
//        return rootView;
//    }
}
