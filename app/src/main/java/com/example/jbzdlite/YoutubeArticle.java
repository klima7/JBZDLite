package com.example.jbzdlite;

import android.support.v4.app.Fragment;

import org.jsoup.nodes.Element;

public class YoutubeArticle extends Article {

    private String videoUrl;

    public YoutubeArticle(Element elem) {
        super(elem);

        Element videoUrlElement = elem.select("iframe[src]").first();
        if(videoUrlElement != null) videoUrl = videoUrlElement.attr("src");
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    @Override
    public String toString() {
        String addition = String.format("[videoUrl=%s]", videoUrl);
        return super.toString() + addition;
    }
}
