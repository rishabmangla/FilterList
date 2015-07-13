package com.rishab.mangla.bikes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rishab.mangla.bikes.app.AppController;
import com.rishab.mangla.bikes.model.Bike;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * An activity representing a list of Items.
 * The activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The list of items is a
 * {@link ItemListFragment} and the item selected list
 * (if present) is a {@link ItemDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link ItemListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class ItemListActivity extends FragmentActivity
        implements ItemListFragment.Callbacks {
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String url = "http://api.testing.bikedekho.com/v1/test/allFilterTag?_format=json";
    private ProgressDialog pDialog;
    private List<Bike> bikeList = new ArrayList<Bike>();
    ArrayList<String> filters = new ArrayList<String>();
    ArrayList<String> filtersLink = new ArrayList<String>();
//    ArrayList<ArrayList<String>> selectionList = new ArrayList<ArrayList<String>>();
    HashMap<String, ArrayList<String>> selectionNameMap = new HashMap<String, ArrayList<String>>();
    HashMap<String, ArrayList<String>> selectionLinkMap = new HashMap<String, ArrayList<String>>();

    HashMap<String, SparseBooleanArray> checkedItemsMap= new HashMap<String, SparseBooleanArray>();
    HashMap<String, ItemDetailFragment> mDetailFragmentsMap = new HashMap<String, ItemDetailFragment>();

    ItemDetailFragment mCurrentDetailFrag;
    String mPrevFragSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        // changing action bar color
        getActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#BE2625")));

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        filters.add("brand");
        filters.add("price_range");
        filters.add("cc");
        filters.add("style");
        filters.add("start_option");
        filters.add("sort_tag");

        filtersLink.add("brand");
        filtersLink.add("price");
        filtersLink.add("cc");
        filtersLink.add("style");
        filtersLink.add("start_option");
        filtersLink.add("sort_tag");

        // Read JSON data
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        Log.i("rishab", "responseAll " + response.toString());
                        hidePDialog();

                        try {
                            Log.i("rishab","response try");
                            Log.i("rishab", "response state: " + response.getString("status"));
                            JSONObject data = response.getJSONObject("data");


                            for(String filter : filters) {
                                JSONArray filterArray = data.getJSONArray(filter);

                                ArrayList<String> array = new ArrayList<String>();
                                ArrayList<String> array2 = new ArrayList<String>();
                                for (int j = 0; j < filterArray.length(); j++) {
                                    array.add(((JSONObject) filterArray.get(j)).getString("name"));
                                    array2.add(((JSONObject) filterArray.get(j)).getString("link_rewrite"));
                                    Log.i("rishab", "name :" + array.get(j));

                                }
                                if(filter.equals("sort_tag"))
                                    Log.i("mangla","link :" + array2);
//                                selectionList.add(array);
                                selectionNameMap.put(filter, array);
                                selectionLinkMap.put(filter, array2);
                            }

                        } catch (JSONException e) {
                            Log.i("rishab","JSONException : " + e);
                            Toast.makeText(ItemListActivity.this, "Check your internet connection", Toast.LENGTH_LONG).show();
                            finish();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.i("rishab","volleyError : " + volleyError.getMessage());
                        Toast.makeText(ItemListActivity.this, "Check your internet connection", Toast.LENGTH_LONG).show();
                        finish();
                        hidePDialog();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String,String>();
                headers.put("X-Public","testing");
                headers.put("X-Hash","testing");
                return headers;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Adds a newly created MessageThreadFragment that is instantiated with the
        // data ConversationThread
        ft.add(R.id.item_list_container, ItemListFragment.newInstance(filters), "asdfgb");
        ft.commit();

        Button applyFilters = (Button) findViewById(R.id.filters_button);
        applyFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentDetailFrag != null){
                    SparseBooleanArray checkedItems = mCurrentDetailFrag.getCheckedItems();
                    checkedItemsMap.put(mPrevFragSelected, checkedItems);
                }

                JSONObject filterJsonObj = new JSONObject();
                for(String filter : filters){
                    if(checkedItemsMap.get(filter) == null || filter.equals("sort_tag"))
                        continue;
                    SparseBooleanArray checkItems = checkedItemsMap.get(filter);
                    JSONObject filterJsonItems = new JSONObject();
                    int count = 0;
                    for(int i = 0; i < selectionNameMap.get(filter).size(); ++i){
                        if(checkItems.get(i) == true){
                            try {
                                filterJsonItems.put(String.valueOf(count++), selectionLinkMap.get(filter).get(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        filterJsonObj.put(filtersLink.get(filters.indexOf(filter)), filterJsonItems);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
Log.i("mangla","json build : " + filterJsonObj.toString());
//                try {
//                    filterJsonObj.put("brand", new JSONObject().put("0", "bajaj"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                JSONObject obj = new JSONObject();
                try {
                    obj.put("filter", filterJsonObj);
                    obj.put("start", "1");
                    obj.put("end", "20");
                    if(checkedItemsMap.get("sort_tag") != null && mDetailFragmentsMap.get("sort_tag") != null)
                       obj.put("sort_type​", selectionLinkMap.get("sort_tag").get(mDetailFragmentsMap.get("sort_tag").getListView().getCheckedItemPosition()));
                } catch (JSONException e) {
                    Log.i("rishab", "mainObj error : " + e);
                    e.printStackTrace();
                }
                Log.i("mangla","json2 build : " + obj.toString());
                Intent intent = new Intent(ItemListActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.ARG_JSON_OBJ,obj.toString());
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    /**
     * Callback method from {@link ItemListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        Log.i("rishab", "onItemSelected id: " + id);
            if(mCurrentDetailFrag != null){
                SparseBooleanArray checkedItems = mCurrentDetailFrag.getCheckedItems();
                if(checkedItems != null)
                    checkedItemsMap.put(mPrevFragSelected, checkedItems);
            }

            if(mDetailFragmentsMap.get(id) == null) {
                mCurrentDetailFrag = new ItemDetailFragment();
                mDetailFragmentsMap.put(id, mCurrentDetailFrag);
                Bundle arguments = new Bundle();
                arguments.putStringArrayList(ItemDetailFragment.ARG_ITEM_ID, selectionNameMap.get(id));
                mCurrentDetailFrag.setArguments(arguments);

            }else
                mCurrentDetailFrag = mDetailFragmentsMap.get(id);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, mCurrentDetailFrag)
                    .commit();
            mPrevFragSelected = id;
    }
}
