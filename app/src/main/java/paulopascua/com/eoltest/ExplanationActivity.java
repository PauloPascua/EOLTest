package paulopascua.com.eoltest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by aaronho on 2/18/17.
 */
public class ExplanationActivity extends Activity {
    private Button nextButton;
    private ScrollView scrollView;
    private TextView explanationText;


    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_explain);
        nextButton = (Button) findViewById(R.id.nextButton);
        explanationText = (TextView) findViewById(R.id.explanationView);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/stratum.otf");
        explanationText.setTypeface(custom_font);
        explanationText.setText("This is the text");
    }

    public void onNext(View v) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        finish();
    }
}
