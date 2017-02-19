package paulopascua.com.eoltest;

/**
 * Created by Paulo on 2017-02-19.
 */

public class DataObjectQuery extends EOLQuery {
    public DataObjectQuery(String id) {
        query += "data_objects/1.0/" + id + ".json?taxonomy=false&cache_ttl=&language=en";
    }
}
