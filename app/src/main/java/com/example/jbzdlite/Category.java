package com.example.jbzdlite;

public class Category {

    private String name;
    private String address;

    public Category(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPageAddress(int nr) {
        return address + "/" + nr;
    }

    @Override
    public String toString() {
        String pattern = "%s[name=%s; address=%s]";
        return String.format(pattern, getClass().getSimpleName(), name, address);
    }
}
