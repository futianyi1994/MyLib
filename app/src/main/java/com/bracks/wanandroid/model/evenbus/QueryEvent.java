package com.bracks.wanandroid.model.evenbus;

/**
 * good programmer.
 *
 * @date : 2019-08-03 上午 11:40
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class QueryEvent {
    private int id;
    private String search;

    public QueryEvent(int id, String search) {
        this.id = id;
        this.search = search;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
