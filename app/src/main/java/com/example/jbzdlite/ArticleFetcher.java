package com.example.jbzdlite;

import android.util.Pair;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ArticleFetcher {

    private final int ARTICLES_PER_PAGE;

    private Category category;
    private int tracePosition;
    private long traceId;

    public ArticleFetcher(Category category) throws IOException {
        ARTICLES_PER_PAGE = getArticlesCountOnPage(category);
        this.category = category;
        mark();
    }

    public Article fetchAbsolute(int nr) throws IOException {
        Elements articles = getArticleElements(nr);

        Element elem = articles.get(nr % ARTICLES_PER_PAGE);
        Article article = Article.createFromElement(elem);
        return article;
    }

    public Pair<Integer, Article[]> fetchAbsoluteBulk(int nr) throws IOException {
        Elements articles = getArticleElements(nr);

        Article[] arr = new Article[articles.size()];
        for(int i=0; i<articles.size(); i++)
            arr[i] = Article.createFromElement(articles.get(i));
        return new Pair(nr/ARTICLES_PER_PAGE*ARTICLES_PER_PAGE, arr);
    }

    private Elements getArticleElements(int nr) throws IOException {
        int page_nr = nr / ARTICLES_PER_PAGE;
        int article_nr = nr % ARTICLES_PER_PAGE;

        String address = category.getPageAddress(page_nr+1);
        Document doc = Jsoup.connect(address).get();
        Elements articles = doc.getElementsByTag("article");
        return articles;
    }

    public int getArticlesCountOnPage(Category category) throws IOException {
        String address = category.getPageAddress(1);
        Document doc = Jsoup.connect(address).get();
        Elements articles = doc.getElementsByTag("article");
        return articles.size();
    }

    public void mark() throws IOException {
        Article first = fetchAbsolute(0);
        traceId = first.getId();
        tracePosition = 0;
    }

    public Article fetchRelative(int nr) throws IOException {
        updateShift();
        return fetchAbsolute(nr + tracePosition);
    }

    public Pair<Integer, Article[]> fetchRelativeBulk(int nr) throws IOException {
        updateShift();
        return fetchAbsoluteBulk(nr + tracePosition);
    }

    private void updateShift() throws IOException {
        Article article = fetchAbsolute(tracePosition);
        while(article.getId() != traceId) {
            tracePosition++;
            article = fetchAbsolute(tracePosition);
        }
    }
}
