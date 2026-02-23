package ru.anastasya.readingportal.models;

public class Chapter {

    private Long id;
    private String title;
    private String content;
    private int chapter_main_number;
    private int chapter_sub_number;
    private Long volume_id;

    public Chapter() {
    }

    public Chapter(String title, int chapter_main_number, int chapter_sub_number, Long volume_id) {
        this.id = null;
        this.title = title;
        this.content = null;
        this.chapter_main_number = chapter_main_number;
        this.chapter_sub_number = chapter_sub_number;
        this.volume_id = volume_id;
    }

    public Chapter(String title, String content, int chapter_main_number, int chapter_sub_number, Long volume_id) {
        this.id = null;
        this.title = title;
        this.content = content;
        this.chapter_main_number = chapter_main_number;
        this.chapter_sub_number = chapter_sub_number;
        this.volume_id = volume_id;
    }

    public Chapter(Long id, String title, int chapter_main_number, int chapter_sub_number, Long volume_id) {
        this.id = id;
        this.title = title;
        this.content = null;
        this.chapter_main_number = chapter_main_number;
        this.chapter_sub_number = chapter_sub_number;
        this.volume_id = volume_id;
    }

    public Chapter(Long id, String title, String content, int chapter_main_number, int chapter_sub_number, Long volume_id) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.chapter_main_number = chapter_main_number;
        this.chapter_sub_number = chapter_sub_number;
        this.volume_id = volume_id;
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", chapter_main_number=" + chapter_main_number +
                ", chapter_sub_number=" + chapter_sub_number +
                ", volume_id=" + volume_id +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getChapter_main_number() {
        return chapter_main_number;
    }

    public void setChapter_main_number(int chapter_main_number) {
        this.chapter_main_number = chapter_main_number;
    }

    public int getChapter_sub_number() {
        return chapter_sub_number;
    }

    public void setChapter_sub_number(int chapter_sub_number) {
        this.chapter_sub_number = chapter_sub_number;
    }

    public Long getVolume_id() {
        return volume_id;
    }

    public void setVolume_id(Long volume_id) {
        this.volume_id = volume_id;
    }
}
