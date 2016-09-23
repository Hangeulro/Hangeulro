package kr.edcan.neologism.model;

/**
 * Created by KOHA_DESKTOP on 2016. 6. 19..
 */
public class CommonRecycleData {
    private String title, content, subContent;
    private boolean isHeader;
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getSubContent() {
        return subContent;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public CommonRecycleData(String title, String content, String subContent, boolean isHeader) {
        this.title = title;
        this.content = content;
        this.subContent = subContent;
        this.isHeader = isHeader;
    }
}
