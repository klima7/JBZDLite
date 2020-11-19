package com.example.jbzdlite;

import java.io.IOException;
import java.util.HashMap;

public class ArticleContainer {

    private HashMap<Integer, Article> articles = new HashMap<>();
    private Category category;
    private ArticleFetcher fetcher;

    public ArticleContainer(Category category) {
        this.category = category;
    }

    public Article getArticle(int nr) throws IOException {
        if(fetcher == null)
            fetcher = new ArticleFetcher(category);

        Article article = articles.get(nr);
        if(article == null) {
            article = fetcher.fetchRelative(nr);
            articles.put(nr, article);
        }
        return article;
    }
}
