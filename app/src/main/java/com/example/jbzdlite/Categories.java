package com.example.jbzdlite;

import android.support.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Categories implements Iterable<Category> {

    public static final Category MAIN_CATEGORY = new Category("Główna", "https://jbzd.com.pl/str");
    public static final Category PENDING_CATEGORY = new Category("Oczekujące", "https://jbzd.com.pl/oczekujace");

    private List<Category> categories = new ArrayList<>();
    private boolean initialized = false;
    private static Categories instance;

    private Categories() {
        categories.addAll(getPrimaryCategories());
    }

    public static Categories get() {
        if(instance == null)
            instance = new Categories();
        return instance;
    }

    public void init() throws IOException {
        if(!initialized) {
            categories.addAll(getExtraCategories());
            initialized = true;
        }
    }

    private List<Category> getPrimaryCategories() {
        List<Category> aux = new ArrayList<>();
        aux.add(MAIN_CATEGORY);
        aux.add(PENDING_CATEGORY);
        return aux;
    }

    private List<Category> getExtraCategories() throws IOException {
        List<Category> aux = new ArrayList<>();

        Document doc = Jsoup.connect(MAIN_CATEGORY.getAddress()).get();
        Elements categories = doc.select("div.nav-categories > div > a");

        for(Element category : categories) {
            String name = category.text();
            String address = category.attr("href");
            aux.add(new Category(name, address));
        }

        return aux;
    }

    public Category getCategory(String name) {
        for(Category category : categories) {
            if(category.getName().equalsIgnoreCase(name))
                return category;
        }
        return null;
    }

    @NonNull
    @Override
    public Iterator<Category> iterator() {
        return categories.iterator();
    }

    @Override
    public String toString() {
        String catStr = "";
        for(int i=0; i<categories.size(); i++) {
            catStr += "\n" + categories.get(i);
            if(i != categories.size()-1)
                catStr += ",";
        }
        return String.format("%s[categories=%s]", getClass().getSimpleName(), catStr);
    }
}
