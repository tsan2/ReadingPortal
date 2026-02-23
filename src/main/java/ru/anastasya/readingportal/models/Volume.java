package ru.anastasya.readingportal.models;

public class Volume {

    private Long id;
    private String title;
    private int volume_main_number;
    private int volume_sub_number;
    private Long book_id;

    public Volume() {

    }

    public Volume(String title, int volume_main_number, int volume_sub_number, Long book_id) {
        this.id = null;
        this.title = title;
        this.volume_main_number = volume_main_number;
        this.volume_sub_number = volume_sub_number;
        this.book_id = book_id;
    }

    public Volume(Long id, String title, int volume_main_number, int volume_sub_number, Long book_id) {
        this.id = id;
        this.title = title;
        this.volume_main_number = volume_main_number;
        this.volume_sub_number = volume_sub_number;
        this.book_id = book_id;
    }

    @Override
    public String toString() {
        return "Volume{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", volume_main_number=" + volume_main_number +
                ", volume_sub_number=" + volume_sub_number +
                ", book_id=" + book_id +
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

    public int getVolume_main_number() {
        return volume_main_number;
    }

    public void setVolume_main_number(int volume_main_number) {
        this.volume_main_number = volume_main_number;
    }

    public int getVolume_sub_number() {
        return volume_sub_number;
    }

    public void setVolume_sub_number(int volume_sub_number) {
        this.volume_sub_number = volume_sub_number;
    }

    public Long getBook_id() {
        return book_id;
    }

    public void setBook_id(Long book_id) {
        this.book_id = book_id;
    }
}
