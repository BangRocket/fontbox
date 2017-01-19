package net.afterlifelochie.fontbox.api.layout;

public interface IPageIndex {

    void push(String id, int page);

    int find(String id);
}
