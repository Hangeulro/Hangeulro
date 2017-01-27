package kr.edcan.neologism.model;

import io.realm.RealmObject;

/**
 * Created by Junseok Oh on 2017-01-27.
 */
public class Tag extends RealmObject {
    public Tag() {

    }

    public Tag(String tagValue) {
        this.tagValue = tagValue;
    }

    private String tagValue;

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }
}
