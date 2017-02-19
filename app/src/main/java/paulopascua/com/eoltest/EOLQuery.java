package paulopascua.com.eoltest;

import android.app.DownloadManager;

/**
 * Created by Paulo on 2017-02-19.
 */

public abstract class EOLQuery {
    String query = "http://eol.org/api/";

    public String getQueryUrl() {
        return query;
    }
}
