package com.rishab.mangla.bikes;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rishab.mangla.bikes.adater.CustomListAdapter;
import com.rishab.mangla.bikes.app.AppController;
import com.rishab.mangla.bikes.model.Bike;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class MainActivity extends Activity {
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String ARG_JSON_OBJ = "obj_id";
    // bikedekho json url
//  private static final String url = "http://api.testing.bikedekho.com/v1/test/allFilterTag?_format=json";
    private static final String url = "http://api.testing.bikedekho.com/v1/test/result?_format=json";

    private ProgressDialog pDialog;
    private List<Bike> bikeList = new ArrayList<Bike>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("mangla","Main onCreate ");
        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, bikeList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // changing action bar color
        getActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#BE2625")));

        JSONObject obj = new JSONObject();
//            JSONObject filterObj = new JSONObject();
//            try {
//                filterObj.put("brand", new JSONObject().put("0", "bajaj").put("1", "honda"));
////            filterObj.put("price",new JSONObject().put("0","15000­rs­30000"));
////            filterObj.put("fuelType",new JSONObject().put("0","Petrol"));
////            filterObj.put("city",new JSONObject().put("0","adilabad"));
////            filterObj.put("style",new JSONObject().put("0","electric"));
////            filterObj.put("start_option",new JSONObject().put("0","kick­start"));
////            filterObj.put("cc",new JSONObject().put("0","less­than­100cc"));
//            } catch (JSONException e) {
//                Log.i("rishab", "filterObj error : " + e);
//                e.printStackTrace();
//            }

            try {
                obj.put("filter", new JSONObject());
                obj.put("start", "1");
                obj.put("end", "20");
                obj.put("sort_type​", "price.asc");
            } catch (JSONException e) {
                Log.i("rishab", "mainObj error : " + e);
                e.printStackTrace();
            }
        readJsonData(obj);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent != null){

            pDialog = new ProgressDialog(this);
            // Showing progress dialog before making http request
            pDialog.setMessage("Loading...");
            pDialog.show();

            JSONObject obj;
            try {
                obj = new JSONObject(intent.getStringExtra(ARG_JSON_OBJ));
                bikeList.clear();
                readJsonData(obj);
                Log.i("mangla", "Main onNewIntent 1");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        Log.i("mangla", "Main onNewIntent 2");
    }

    private void readJsonData(JSONObject obj){
        // Read JSON data
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        Log.i("rishab", "response " + response.toString());
                        hidePDialog();
                        try {
                            Log.i("rishab","response try");
                            Log.i("rishab", "response state: " + response.getString("status"));
                            JSONObject data = response.getJSONObject("data");

                            String count = data.getString("count");
                            Log.i("rishab","bike count " + count);

                            JSONArray newBikes = data.getJSONArray("newBikes");
                            for (int i = 0; i < newBikes.length(); i++) {
                                JSONObject newBike = (JSONObject) newBikes.get(i);
                                Bike bike = new Bike();
                                bike.setTitle(newBike.getString("display_name"));
                                bike.setThumbnailUrl(newBike.getString("image"));
                                bike.setCapacity(newBike.getString("engineCapacity"));
                                bike.setPrice(newBike.getString("price"));

                                // adding bikes to bike array
                                bikeList.add(bike);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.i("rishab", "volleyErrorrr : " + volleyError);
                        hidePDialog();
                        Toast.makeText(MainActivity.this, "Check your internet connection", Toast.LENGTH_LONG).show();
//                        finish();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String,String>();
//                headers.put("X-Public","testing");
//                headers.put("X-Hash","testing");
//                headers.put("Content-Type","application/json");
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bike_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_filters:
                Intent intent = new Intent(this, ItemListActivity.class);
                startActivity(intent);


        }
        return super.onOptionsItemSelected(item);
    }
}