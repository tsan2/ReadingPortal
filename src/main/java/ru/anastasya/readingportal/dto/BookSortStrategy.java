package ru.anastasya.readingportal.dto;

public enum BookSortStrategy {

    NEWEST("ORDER BY created_at DESC", "Сортировка по новизне"),
    ALPHABETICAL("ORDER BY title", "Сортировка по алфавиту");

    private final String description;
    private final String sql;

    BookSortStrategy(String sql, String description){
        this.sql = sql;
        this.description = description;
    }

    public String getSql() {
        return sql;
    }

    public String getDescription(){
        return description;
    }
}
