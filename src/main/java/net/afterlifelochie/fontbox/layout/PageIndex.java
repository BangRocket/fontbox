package net.afterlifelochie.fontbox.layout;

import net.afterlifelochie.fontbox.api.layout.IPageIndex;

import java.util.HashMap;

public class PageIndex implements IPageIndex {
    private final HashMap<String, Integer> ids;

    public PageIndex() {
        ids = new HashMap<>();
    }

    @Override
    public void push(String id, int page) {
        ids.put(id, page);
    }

    @Override
    public int find(String id) {
        return (ids.containsKey(id)) ? ids.get(id) : -1;
    }

}
