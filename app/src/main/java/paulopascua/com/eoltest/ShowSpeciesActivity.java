package paulopascua.com.eoltest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class ShowSpeciesActivity extends AppCompatActivity implements LoadImageTask.Listener {
    ViewGroup layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_species);

        Intent intent = getIntent();
        String jsonMessage = intent.getStringExtra(SpeciesList.EOL_QUERY_MESSAGE);

        layout = (ViewGroup) findViewById(R.id.activity_show_species);

        // parse JSON
        try {
            JSONObject reader = new JSONObject(jsonMessage);

            /*loadCanonicalName(reader);
            loadVernacularName(reader); */
            loadImage2(reader);
            // loadDescription(reader);
        } catch (JSONException e) {
            // render error messages
            e.printStackTrace();
        }
    }

    private void loadCanonicalName(JSONObject reader) {
        TextView canonicalNameView = new TextView(this);

        try {
            JSONArray taxonConcepts = reader.getJSONArray("taxonConcepts");
            JSONObject preferredCanonicalForm = taxonConcepts.getJSONObject(0);
            String canonicalName = preferredCanonicalForm.getString("canonicalForm");
            canonicalNameView.setText(canonicalName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        layout.addView(canonicalNameView);
    }

    private void loadVernacularName(JSONObject reader) {
        TextView vernacularNameView = new TextView(this);

        try {
            // Vernacular name (blank if no preferred English name)
            JSONArray vernacularNames = reader.getJSONArray("vernacularNames");
            // find preferred English name
            String vernacularName = "default";
            for (int i = 0; i < vernacularNames.length(); i++) {
                JSONObject currName = vernacularNames.getJSONObject(i);

                if (currName.getString("language").equals("en")) {
                    if (currName.has("eol_preferred") && currName.getBoolean("eol_preferred")) {
                        vernacularName = currName.getString("vernacularName");
                    }
                }
            }

            vernacularNameView.setText(vernacularName);
        } catch(JSONException e) {
            e.printStackTrace();
        }

        layout.addView(vernacularNameView);
    }

    private void loadImage(JSONObject reader) {
        String purlImage = "http://purl.org/dc/dcmitype/StillImage";

        try {
            // Find image and description
            JSONArray dataObjects = reader.getJSONArray("dataObjects");
            String imageId = "";

            for (int i = 0; i < dataObjects.length(); i++) {
                JSONObject currObject = dataObjects.getJSONObject(i);

                if (imageId.equals("") &&
                        currObject.getString("dataType").equals(purlImage)) {
                    imageId = currObject.get("dataObjectVersionID").toString();
                }
            }

            ImageView imageView = new ImageView(this);


            if (imageId.equals("")) {
                imageView.setImageResource(R.drawable.default_image);
            } else {
                Toast.makeText(this, "LOL here", Toast.LENGTH_SHORT).show();
                EOLQuery imageQuery = new DataObjectQuery(imageId);
                QueryTask queryImageTask = new QueryTask(imageQuery);

                try {
                    String json = queryImageTask.execute().get();
                    JSONObject innerJSONObject = new JSONObject(json);

                    JSONArray innerDataObjects = innerJSONObject.getJSONArray("dataObjects");
                    JSONObject imageObject = innerDataObjects.getJSONObject(0);

                    URL imageURL = new URL(imageObject.getString("mediaURL"));

                    TextView testView = new TextView(this);
                    testView.setText(imageURL.toString());
                    layout.addView(testView);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                } catch (ExecutionException e1) {
                    e1.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            layout.addView(imageView);
        } catch(JSONException e) {
            e.printStackTrace();
        }

    }

    private void loadImage2(JSONObject reader) {
        String purlImage = "http://purl.org/dc/dcmitype/StillImage";
        int defaultImage = R.drawable.default_image;

        try {
            // Find image and description
            JSONArray dataObjects = reader.getJSONArray("dataObjects");
            String imageId = "";

            for (int i = 0; i < dataObjects.length(); i++) {
                JSONObject currObject = dataObjects.getJSONObject(i);

                if (imageId.equals("") &&
                        currObject.getString("dataType").equals(purlImage)) {
                    imageId = currObject.get("dataObjectVersionID").toString();
                }
            }

            ImageView imageView = new ImageView(this);


            if (imageId.equals("")) {
                imageView.setImageResource(defaultImage);
            } else {
                EOLQuery imageQuery = new DataObjectQuery(imageId);
                QueryTask queryImageTask = new QueryTask(imageQuery);

                try {
                    String json = queryImageTask.execute().get();
                    JSONObject innerJSONObject = new JSONObject(json);

                    JSONArray innerDataObjects = innerJSONObject.getJSONArray("dataObjects");
                    JSONObject imageObject = innerDataObjects.getJSONObject(0);

                    String imageURL = imageObject.getString("mediaURL");

                    ImageLoader imgLoader = new ImageLoader(getApplicationContext());
                    imgLoader.DisplayImage(imageURL, defaultImage, imageView);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                } catch (ExecutionException e1) {
                    e1.printStackTrace();
                }
            }

            layout.addView(imageView);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadDescription(JSONObject reader) {
        String purlText = "http://purl.org/dc/dcmitype/Text";

        try {
            JSONArray dataObjects = reader.getJSONArray("dataObjects");
            String descriptionId = "";
            String description = "No description available.";

            for (int i = 0; i < dataObjects.length(); i++) {
                JSONObject currObject = dataObjects.getJSONObject(i);

                // if it is a description, set description id
                if (descriptionId.equals("") &&
                        currObject.getString("dataType").equals(purlText)) {
                    descriptionId = currObject.get("dataObjectVersionID").toString();
                }

                TextView descriptionView = new TextView(this);
                if (!descriptionId.equals("")) {
                    EOLQuery descriptionQuery = new DataObjectQuery(descriptionId);
                    QueryTask queryDescriptionTask = new QueryTask(descriptionQuery);

                    try {
                        String json = queryDescriptionTask.execute().get();
                        JSONObject innerJSONObject = new JSONObject(json);

                        JSONArray innerDataObjects = innerJSONObject.getJSONArray("dataObjects");
                        JSONObject descriptionObject = innerDataObjects.getJSONObject(0);

                        description = descriptionObject.getString("description");
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    } catch (ExecutionException e1) {
                        e1.printStackTrace();
                    }
                }
                descriptionView.setText(description);

                layout.addView(descriptionView);
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) {

    }

    @Override
    public void onError() {

    }
}
