package ru.anastasya.readingportal.dao;

import ru.anastasya.readingportal.models.Chapter;
import ru.anastasya.readingportal.utils.CRUDutil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ChapterDAO {

    private static final ChapterDAO INSTANCE = new ChapterDAO();

    private ChapterDAO(){}

    public static ChapterDAO getInstance(){
        return INSTANCE;
    }


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
    private static final String DELETE_ALL_CHAPTERS_BY_VOLUME_ID_SQL = "DELETE FROM chapters WHERE volume_id = ?;";
    private static final String FIND_LAST_MAIN_CHAPTER_NUMBER_SQL = """
            SELECT MAX(chapter_main_number)
            FROM chapters
            WHERE volume_id = ?;""";
    private static final String FIND_INFO_CHAPTER_BY_ID_SQL = """
            SELECT id, title, chapter_main_number, chapter_sub_number, volume_id
            FROM chapters WHERE id = ?;""";
    private static final String FIND_FULL_CHAPTER_BY_ID_SQL = """
            SELECT id, title, content, chapter_main_number, chapter_sub_number, volume_id
            FROM chapters WHERE id = ?;""";
    private static final String FIND_ALL_INFO_CHAPTERS_BY_VOLUME_ID_SQL = """
            SELECT id, title, chapter_main_number, chapter_sub_number, volume_id
            FROM chapters WHERE volume_id = ?;""";
    private static final String EXISTS_CHAPTER_NUMBER_SQL = """
            SELECT COUNT(*) FROM chapters
            WHERE volume_id = ? AND chapter_main_number = ? AND chapter_sub_number = ?;""";

    public Long save(Chapter chapter){
        Objects.requireNonNull(chapter, "Нельзя сохранить null chapter");
        return CRUDutil.insert(SAVE_CHAPTER_SQL, chapter.getTitle(), chapter.getContent(),
                chapter.getChapterMainNumber(), chapter.getChapterSubNumber(), chapter.getVolumeId());
    }

    public void updateMetadata(Chapter chapter){
        Objects.requireNonNull(chapter, "Нельзя изменить null chapter");
        CRUDutil.update(UPDATE_CHAPTER_METADATA_SQL, chapter.getTitle(), chapter.getChapterMainNumber(),
                chapter.getChapterSubNumber(), chapter.getId());
    }

    public void updateContent(Long id, String text){
        CRUDutil.update(UPDATE_CHAPTER_CONTENT_SQL, text, id);
    }

    public void delete(Long chapterId){
        CRUDutil.update(DELETE_CHAPTER_SQL, chapterId);
    }

    public void deleteAllByVolumeId(Long volumeId){
        CRUDutil.update(DELETE_ALL_CHAPTERS_BY_VOLUME_ID_SQL, volumeId);
    }

    public int findLastMainNumberByVolumeId(Long volumeId){
        return CRUDutil.readOne(FIND_LAST_MAIN_CHAPTER_NUMBER_SQL, rs -> rs.getInt(1), volumeId);
    }

    public Chapter findInfoById(Long id){
        return CRUDutil.readOne(FIND_INFO_CHAPTER_BY_ID_SQL, this::infoMap, id);
    }

    public Chapter findFullById(Long id){
        return CRUDutil.readOne(FIND_FULL_CHAPTER_BY_ID_SQL, this::fullMap, id);
    }

    public List<Chapter> findAllInfoByVolumeId(Long volumeId){
        return CRUDutil.readMany(FIND_ALL_INFO_CHAPTERS_BY_VOLUME_ID_SQL, this::infoMap, volumeId);
    }

    public boolean existsChapterNumber(Long bookId, int chapterMainNumber, int chapterSubNumber){
        int count = CRUDutil.readOne(EXISTS_CHAPTER_NUMBER_SQL, rs -> rs.getInt(1),
                bookId, chapterMainNumber, chapterSubNumber);
        return count>0;
    }

    private Chapter infoMap(ResultSet resultSet) throws SQLException{
        Long id = resultSet.getLong("id");
        String title = resultSet.getString("title");
        int chapter_main_number = resultSet.getInt("chapter_main_number");
        int chapter_sub_number = resultSet.getInt("chapter_sub_number");
        Long volume_id = resultSet.getLong("volume_id");
        return new Chapter(id, title, chapter_main_number, chapter_sub_number, volume_id);
    }

    private Chapter fullMap(ResultSet resultSet) throws SQLException{
        Long id = resultSet.getLong("id");
        String title = resultSet.getString("title");
        String content = resultSet.getString("content");
        int chapter_main_number = resultSet.getInt("chapter_main_number");
        int chapter_sub_number = resultSet.getInt("chapter_sub_number");
        Long volume_id = resultSet.getLong("volume_id");
        return new Chapter(id, title, content, chapter_main_number, chapter_sub_number, volume_id);
    }

}
