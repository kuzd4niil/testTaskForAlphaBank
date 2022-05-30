package org.kuzd4niil.testTaskForAlphaBank.entities;

public class Gif {
    private Long id;
    private String url;

    private String data;

    public Gif(Long id, String url, String data) {
        this.id = id;
        this.url = url;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
