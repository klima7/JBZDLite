package com.example.jbzdlite;

import android.support.v4.app.Fragment;

import org.jsoup.nodes.Element;

public class VideoArticle extends Article {

    private String previewUrl;
    private String videoUrl;

    public VideoArticle(Element elem) {
        super(elem);

        // previewUrl
        Element videoUrlElement = elem.select("video > source").first();
        if(videoUrlElement != null)
            videoUrl = videoUrlElement.attr("src");

        // videoUrl
        Element setupElement = elem.select("video").first();
        if(setupElement != null) {
            String setupString = setupElement.attr("data-setup");
            if(setupString != null) {
                int endOfPoster = setupString.indexOf("poster") + 7;
                int firstQuoteIndex = setupString.indexOf("\"", endOfPoster);
                int secondQuoteIndex = setupString.indexOf("\"", firstQuoteIndex + 1);
                previewUrl = setupString.substring(firstQuoteIndex + 1, secondQuoteIndex).replaceAll("\\\\", "");
            }
        }
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    @Override
    public String toString() {
        String addition = String.format("[previewUrl=%s; videoUrl=%s]", previewUrl, videoUrl);
        return super.toString() + addition;
    }
}
