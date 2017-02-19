package paulopascua.com.eoltest;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;


public class MapsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

    }

    public void onMapClick(View v) {
        Intent intent = new Intent(this, SpeciesList.class);
        startActivity(intent);
        finish();
    }
}
