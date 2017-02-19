package paulopascua.com.eoltest;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Paulo on 2017-02-18.
 */

public class QueryTask extends AsyncTask<String, Integer, String> {
    static EOLQuery eolQuery;

    public QueryTask(EOLQuery eolQuery) {
        this.eolQuery = eolQuery;
    }

    @Override
    protected String doInBackground(String... strings) {
        String retriever = "";

        try {
            retriever = getXML();
            return retriever;
        } catch (Exception e) {
            e.printStackTrace();
        }

        retriever = "Error fetching page.";
        return retriever;
    }

    // Only returns one record. Expand to accommodate array
    private static String getXML() throws Exception {
        String queryUrl = eolQuery.getQueryUrl();

        BufferedReader reader = null;
        URLConnection uc = null;

        try {
            URL url = new URL(queryUrl);
            uc = url.openConnection();
            uc.connect();
            reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            queryUrl = buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }

        return queryUrl;
    }
}