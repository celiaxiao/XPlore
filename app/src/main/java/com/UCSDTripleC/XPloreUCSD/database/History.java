package com.UCSDTripleC.XPloreUCSD.database;

/**
 * A helper class that holds information about the history of a landmark
 */
public class History {

    private String title;
    private String articleType;
    private String content;

    public History(String title, String articleType, String content) {
        this.title = title;
        this.articleType = articleType;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getArticleType() {
        return articleType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



}
