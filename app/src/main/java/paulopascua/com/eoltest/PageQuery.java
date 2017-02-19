package paulopascua.com.eoltest;

/**
 * Created by Paulo on 2017-02-19.
 */

public class PageQuery extends EOLQuery {
    public PageQuery(String id) {
        query += "pages/1.0.json?id=" + id + "&iucn=true&common_names=true&language=en&vetted=1";
    }
}
