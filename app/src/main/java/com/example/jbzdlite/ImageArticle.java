package com.example.jbzdlite;

import org.jsoup.nodes.Element;

public class ImageArticle extends Article {

    private String imageUrl;

    public ImageArticle(Element elem) {
        super(elem);

        // imageUrl
        Element imageUrlElement = elem.select("img.article-image").first();
        if(imageUrlElement != null)
            imageUrl = imageUrlElement.attr("src");
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String toString() {
        String addition = String.format("[imageUrl=%s]", imageUrl);
        return super.toString() + addition;
    }
}
