package paulopascua.com.eoltest;

import android.widget.BaseAdapter;

/**
 * Created by Paulo on 2017-02-19.
 */

import android.app.Activity;
import android.content.ClipData;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class SpeciesAdapter extends BaseAdapter {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    private Activity context;
    private ArrayList<Species> species;

    public SpeciesAdapter(Activity a, ArrayList<Species> specie) {
        context = a;
        species = specie;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = context.getLayoutInflater().inflate(R.layout.species_view_data, null);

        TextView nameText = (TextView) v.findViewById((R.id.speciesNameView));
        TextView scientificNameText = (TextView) v.findViewById((R.id.speciesScientificNameView));

        ImageView speciesImage = (ImageView) v.findViewById(R.id.speciesImage);

        prefs = context.getSharedPreferences("species_data", context.MODE_PRIVATE);

        Bitmap btMap = BitmapFactory.decodeFile(prefs.getString("pictureValue", ""));

        Species s = species.get(position);
        nameText.setText(s.getName());
        scientificNameText.setText(s.getScientificName());
        speciesImage.setImageBitmap(btMap);


//        Button btnDelete = (Button) v.findViewById(R.id.btnPersonDelete);
//        btnDelete.setTag(s);

        return v;

    }
}
