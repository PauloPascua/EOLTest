package paulopascua.com.eoltest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SpeciesList extends AppCompatActivity {

    public SpeciesAdapter speciesAdapter;
    public ArrayList<Species> speciesArray = new ArrayList<>();
    private ListView speciesList;
    private EditText queryText;
    private Button searchButton;
    public static final String EOL_QUERY_MESSAGE = "paulopascua.com.eoltest.EOL_QUERY_MESSAGE";

    SharedPreferences speciesPrefs;
    SharedPreferences.Editor speciesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.species_list_view);

        speciesList = (ListView) findViewById(R.id.species_lv);
        SpeciesAdapter speciesAdapter = new SpeciesAdapter(this, speciesArray);
        speciesList.setAdapter(speciesAdapter);

        queryText = (EditText) findViewById(R.id.queryText);
        searchButton = (Button) findViewById(R.id.searchButton);

        speciesPrefs = getSharedPreferences("species_data", MODE_PRIVATE);
        speciesEditor = speciesPrefs.edit();
    }

    public void onSearch(View v) {
        Intent intent = new Intent(this, ShowSpeciesActivity.class);


        String query = queryText.getText().toString();
        String json = "";
        JSONObject queryObj = null;

        EOLQuery eolQuery = new PageQuery(query);
        QueryTask queryTask = new QueryTask(eolQuery);
        try {
            json = queryTask.execute().get();
            queryObj = new JSONObject(json);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // Variables
        String scientificName = "";
        String name = "";
        String imageURL = "";
        String description = "";

        // scientific name
        try {
            JSONArray taxonConcepts = queryObj.getJSONArray("taxonConcepts");
            JSONObject preferredName = taxonConcepts.getJSONObject(0);
            scientificName = preferredName.getString("canonicalForm");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // name
        try {
            JSONArray vernacularNames = queryObj.getJSONArray("vernacularNames");
            name = scientificName;
            for (int i = 0; i < vernacularNames.length(); i++) {
                JSONObject currName = vernacularNames.getJSONObject(i);

                if (currName.getString("language").equals("en")) {
                    if (currName.has("eol_preferred") && currName.getBoolean("eol_preferred")) {
                        name = currName.getString("vernacularName");
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // image url
        try {
            // Find image and description
            JSONArray dataObjects = queryObj.getJSONArray("dataObjects");
            String purlImage = "http://purl.org/dc/dcmitype/StillImage";
            String imageId = "";

            for (int i = 0; i < dataObjects.length(); i++) {
                JSONObject currObject = dataObjects.getJSONObject(i);

                if (imageId.equals("") &&
                        currObject.getString("dataType").equals(purlImage)) {
                    imageId = currObject.get("dataObjectVersionID").toString();
                }
            }

            if (!imageId.equals("")) {
                EOLQuery imageQuery = new DataObjectQuery(imageId);
                QueryTask queryImageTask = new QueryTask(imageQuery);

                try {
                    String jsonQuery = queryImageTask.execute().get();
                    JSONObject innerJSONObject = new JSONObject(jsonQuery);

                    JSONArray innerDataObjects = innerJSONObject.getJSONArray("dataObjects");
                    JSONObject imageObject = innerDataObjects.getJSONObject(0);

                    imageURL = imageObject.getString("mediaURL");
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                } catch (ExecutionException e1) {
                    e1.printStackTrace();
                }
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }

        // description
        try {
            JSONArray dataObjects = queryObj.getJSONArray("dataObjects");
            String descriptionId = "";
            description = "No description available.";
            String purlText = "http://purl.org/dc/dcmitype/Text";

            for (int i = 0; i < dataObjects.length(); i++) {
                JSONObject currObject = dataObjects.getJSONObject(i);

                // if it is a description, set description id
                if (descriptionId.equals("") &&
                        currObject.getString("dataType").equals(purlText)) {
                    descriptionId = currObject.get("dataObjectVersionID").toString();
                }

                if (!descriptionId.equals("")) {
                    EOLQuery descriptionQuery = new DataObjectQuery(descriptionId);
                    QueryTask queryDescriptionTask = new QueryTask(descriptionQuery);

                    try {
                        String jsonDescription = queryDescriptionTask.execute().get();
                        JSONObject innerJSONObject = new JSONObject(jsonDescription);

                        JSONArray innerDataObjects = innerJSONObject.getJSONArray("dataObjects");
                        JSONObject descriptionObject = innerDataObjects.getJSONObject(0);

                        description = descriptionObject.getString("description");
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    } catch (ExecutionException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Species species = new Species();
        species.setName(name);
        species.setScientificName(scientificName);
        species.setPicture(imageURL);
        species.setDescription(description);

        speciesArray.add(species);
//        speciesAdapter.notifyDataSetChanged();

        intent.putExtra(EOL_QUERY_MESSAGE, json);
        startActivity(intent);
    }
}
