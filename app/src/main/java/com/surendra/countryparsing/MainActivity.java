//
/*
	Developer : Surendra Gupta
	Date : 16-Aug-2020
	Main process to extra records from JSON file and display in listview
**/

package com.surendra.countryparsing;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ListActivity {

    // URL to get contacts JSON
    private static String url = "https://drive.google.com/file/d/1c-rru5QfswC4rSJJOh4iktI8nKr0le3c/view?usp=sharing";

    // JSON Node names
    private static final String TAG_TITLETOP = "title";
    private static final String TAG_ROW = "row";
	private static final String TAG_SUBTITLE = "title";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_IMAGE = "imageHref";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Calling async task to get json
        new GetStudents().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetStudents extends AsyncTask<Void, Void, Void> {

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> countryList;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            // Making a request to url and getting response
            String jsonStr = webreq.makeWebServiceCall(url, WebRequest.GET);

            Log.d("Response: ", "> " + jsonStr);

            //studentList = ParseJSON(jsonStr); by surendra
			countryList = ParseJSON(jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, countryList,
                    R.layout.list_item, new String[]{TAG_SUBTITLE, TAG_DESCRIPTION,
                    TAG_IMAGE}, new int[]{R.id.title,
                    R.id.Description, R.id.imageHref});

            setListAdapter(adapter);
        }

    }

    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {
                // Hashmap for ListView
                ArrayList<HashMap<String, String>> countryList = new ArrayList<HashMap<String, String>>();

                JSONObject jsonObj = new JSONObject(json);

                // Getting JSON Array node
                JSONArray country = jsonObj.getJSONArray(TAG_TITLETOP);

                // looping through All COUNTRY
                for (int i = 0; i < country.length(); i++) {
                    JSONObject c = country.getJSONObject(i);

                    String title = c.getString(TAG_TITLETOP);
                 
                    


                    // tmp hashmap for single country
                    HashMap<String, String> country = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    country.put(TAG_SUBTITLE, title );
                    country.put(TAG_DESCRIPTION, description);
                    country.put(TAG_IMAGE, imageHref);
                    

                    // adding records to country list
                    countryList.add(country);
                }
                return countryList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
            return null;
        }
    }

}
