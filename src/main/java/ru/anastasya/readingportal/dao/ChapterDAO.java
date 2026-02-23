package ru.anastasya.readingportal.dao;

import ru.anastasya.readingportal.models.Chapter;
import ru.anastasya.readingportal.models.Volume;
import ru.anastasya.readingportal.utils.CRUDutil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ChapterDAO {

    private static final String UPDATE_CHAPTER_METADATA_SQL = """
            UPDATE chapters
            SET title = ?,
            chapter_main_number = ?,
            chapter_sub_number = ?
            WHERE id = ?;
            """;
    private static final String UPDATE_CHAPTER_CONTENT_SQL = """
            UPDATE chapters
            SET content = ?
            WHERE id = ?;
            """;
    private static final String SAVE_CHAPTER_SQL = """
            INSERT INTO chapters(title, content, chapter_main_number, chapter_sub_number, volume_id)
            VALUES (?, ?, ?, ?, ?);""";
    private static final String DELETE_CHAPTER_SQL = "DELETE FROM chapters WHERE id = ?;";
    private static final String FIND_LAST_MAIN_CHAPTER_NUMBER_SQL = """
            SELECT MAX(chapter_main_number)
            FROM chapters
            WHERE volume_id = ?;""";
    private static final String FIND_INFO_CHAPTER_BY_ID_SQL = """
            SELECT id, title, chapter_main_number, chapter_sub_number, volume_id 
            FROM chapters WHERE id = ?;""";
    private static final String FIND_ALL_VOLUMES_BY_BOOK_ID_SQL = "SELECT * FROM volumes WHERE book_id = ?;";

    public void save(Chapter chapter){
        Objects.requireNonNull(chapter, "Нельзя сохранить null chapter");
        CRUDutil.insert(SAVE_CHAPTER_SQL, chapter.getTitle(), chapter.getContent(),
                chapter.getChapter_main_number(), chapter.getChapter_sub_number(), chapter.getVolume_id());
    }

    public void updateMetadata(Chapter chapter){
        Objects.requireNonNull(chapter, "Нельзя изменить null chapter");
        CRUDutil.update(UPDATE_CHAPTER_METADATA_SQL, chapter.getTitle(), chapter.getChapter_main_number(),
                chapter.getChapter_sub_number(), chapter.getId());
    }

    public void updateContent(Long id, String text){
        CRUDutil.update(UPDATE_CHAPTER_CONTENT_SQL, text, id);
    }

    public void delete(Long chapterId){
        CRUDutil.update(DELETE_CHAPTER_SQL, chapterId);
    }

    public int findLastMainNumberByVolumeId(Long volumeId){
        return CRUDutil.readOne(FIND_LAST_MAIN_CHAPTER_NUMBER_SQL, rs -> rs.getInt(1), volumeId);
    }

    public Chapter findInfoById(Long id){
        return CRUDutil.readOne(FIND_INFO_CHAPTER_BY_ID_SQL, this::infoMap, id);
    }
//переделай всё тут
//    public List<Volume> findAllByBookId(Long bookId){
//        return CRUDutil.readMany(FIND_ALL_VOLUMES_BY_BOOK_ID_SQL, this::Map, bookId);
//    }

    private Chapter infoMap(ResultSet resultSet) throws SQLException{
        Long id = resultSet.getLong("id");
        String title = resultSet.getString("title");
        int chapter_main_number = resultSet.getInt("chapter_main_number");
        int chapter_sub_number = resultSet.getInt("chapter_sub_number");
        Long volume_id = resultSet.getLong("volume_id");
        return new Chapter(id, title, chapter_main_number, chapter_sub_number, volume_id);
    }

}
