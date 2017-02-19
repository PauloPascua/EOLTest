package paulopascua.com.eoltest;

/**
 * Created by Paulo on 2017-02-19.
 */

public class Species {

    public void setName(String name) {
        this.name = name;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String name, scientificName, picture, description;

    public String getName() {
        return name;
    }
    //insert fields here based on what details you where able to extract from EOL
}
