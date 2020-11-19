package com.example.jbzdlite;

import android.support.v4.app.Fragment;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public abstract class Article {

    private long id;
    private String title;
    private String description;
    private int pluses;
    private int comments;
    private List<String> tags = new ArrayList<>();
    private String url;

    public Article(Element elem) {

        // id
        String idAttr = elem.attr("data-content-id");
        if (idAttr != null)
            id = Integer.parseInt(idAttr);

        // title
        Element titleElement = elem.select("h3.article-title").first();
        if(titleElement != null)
            title = titleElement.text();

        // url
        Element urlElement = elem.select("h3.article-title > a").first();
        if(urlElement != null)
            url = urlElement.attr("href");

        // pluses
        Element plusesElement = elem.select("vote").first();
        if(plusesElement != null) {
            String plusesAttr = plusesElement.attr(":score");
            if(plusesAttr!=null)
                pluses = Integer.parseInt(plusesAttr);
        }

        // comments
        Element commentsElement = elem.select("div.article-comments-count").first();
        if(commentsElement != null)
            comments = Integer.parseInt(commentsElement.text());

        // tags
        Element tagElemsElement = elem.select("tags").first();
        if(tagElemsElement != null) {
            for(Element tagElem : tagElemsElement.children())
                tags.add(tagElem.text());
        }

        // description
        Element descriptionElement = elem.select("p.article-description").first();
        if(descriptionElement != null) {
            description = descriptionElement.text();
        }
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPluses() {
        return pluses;
    }

    public int getComments() {
        return comments;
    }

    public String[] getTags() {
        String[] array = new String[tags.size()];
        array = tags.toArray(array);
        return array;
    }

    public String getUrl() {
        return url;
    }

    public static Article createFromElement(Element elem) {
        if(elem.select("iframe").size() != 0)
            return new YoutubeArticle(elem);
        else if(elem.select("video").size() != 0)
            return new VideoArticle(elem);
        else
            return new ImageArticle(elem);
    }

    @Override
    public String toString() {
        String pattern = "%s[id=%d; title=%s; pluses=%d; comments=%d; tags=%s; url=%s]";

        String tagsString = "";
        for(int i=0; i<tags.size(); i++) {
            tagsString += tags.get(i);
            if(i != tags.size()-1)
                tagsString += " ";
        }

        return String.format(pattern, getClass().getSimpleName(), id, title, pluses, comments, tagsString, url);
    }
}
