package ru.anastasya.readingportal.dto;

public enum BookSortStrategy {

    NEWEST("ORDER BY created_at DESC"),
    ALPHABETICAL("ORDER BY title");

    private final String sql;

    BookSortStrategy(String sql){
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }
}
