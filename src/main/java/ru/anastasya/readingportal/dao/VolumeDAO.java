package ru.anastasya.readingportal.dao;

import ru.anastasya.readingportal.models.Volume;
import ru.anastasya.readingportal.utils.CRUDutil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class VolumeDAO {
    //сделать потом чтобы была подсказка к номеру главы или как то так
    private static final String UPDATE_VOLUME_SQL = """
            UPDATE volumes
            SET title = ?,
            volume_main_number = ?,
            volume_sub_number = ?
            WHERE id = ?;
            """;
    private static final String SAVE_VOLUME_SQL = """
            INSERT INTO volumes(title, volume_main_number, volume_sub_number, book_id, is_default)
            VALUES (?, ?, ?, ?, ?);""";
    private static final String DELETE_VOLUME_SQL = "DELETE FROM volumes WHERE id = ?;";
    private static final String DELETE_ALL_VOLUMES_BY_BOOK_ID_SQL = "DELETE FROM volumes WHERE book_id = ?;";
    private static final String FIND_LAST_MAIN_VOLUME_NUMBER_SQL = """
            SELECT MAX(volume_main_number)
            FROM volumes
            WHERE book_id = ?;""";
    private static final String FIND_VOLUME_BY_ID_SQL = "SELECT * FROM volumes WHERE id = ?;";
    private static final String FIND_ALL_VOLUMES_BY_BOOK_ID_SQL = "SELECT * FROM volumes WHERE book_id = ?;";
    private static final String FIND_DEFAULT_VOLUME_BY_BOOK_ID_SQL = "SELECT * FROM volumes WHERE book_id = ? and is_default = ?";
    private static final String EXISTS_VOLUME_NUMBER_SQL = """
            SELECT COUNT(*) FROM volumes
            WHERE book_id = ? AND volume_main_number = ? AND volume_sub_number = ?;""";
    private static final String GET_VOLUME_COUNT_BY_BOOK_ID_SQL = """
            SELECT COUNT(*) FROM volumes
            WHERE book_id = ? and is_default = ?;""";

    public Long save(Volume volume){
        Objects.requireNonNull(volume, "Нельзя сохранить null volume");
        return CRUDutil.insert(SAVE_VOLUME_SQL, volume.getTitle(), volume.getVolumeMainNumber(),
                volume.getVolumeSubNumber(), volume.getBookId(), volume.isIs_default());
    }

    public void update(Volume volume){
        Objects.requireNonNull(volume, "Нельзя изменить null volume");
        CRUDutil.update(UPDATE_VOLUME_SQL, volume.getTitle(), volume.getVolumeMainNumber(),
                volume.getVolumeSubNumber(), volume.getId());
    }

    public void delete(Long volumeId){
        CRUDutil.update(DELETE_VOLUME_SQL, volumeId);
    }

    public void deleteAllByBookId(Long bookId){
        CRUDutil.update(DELETE_ALL_VOLUMES_BY_BOOK_ID_SQL, bookId);
    }

    public int findLastMainNumberByBookId(Long bookId){
        return CRUDutil.readOne(FIND_LAST_MAIN_VOLUME_NUMBER_SQL, rs -> rs.getInt(1), bookId);
    }

    public Volume findById(Long id){
        return CRUDutil.readOne(FIND_VOLUME_BY_ID_SQL, this::Map, id);
    }

    public List<Volume> findAllByBookId(Long bookId){
        return CRUDutil.readMany(FIND_ALL_VOLUMES_BY_BOOK_ID_SQL, this::Map, bookId);
    }

    public Long createDefaultByBookId(Long bookId){
        return CRUDutil.insert(SAVE_VOLUME_SQL, "Базовый том", 0, 0, bookId, true);
    }

    public Volume findDefaultByBookId(Long bookId){
        return CRUDutil.readOne(FIND_DEFAULT_VOLUME_BY_BOOK_ID_SQL, this::Map, bookId, true);
    }

    public int getVolumeCountByBookId(Long bookId){
        return CRUDutil.readOne(GET_VOLUME_COUNT_BY_BOOK_ID_SQL, rs -> rs.getInt(1), bookId, false);
    }

    public boolean existsVolumeNumber(Long bookId, int volumeMainNumber, int volumeSubNumber){
        int count = CRUDutil.readOne(EXISTS_VOLUME_NUMBER_SQL, rs -> rs.getInt(1),
                bookId, volumeMainNumber, volumeSubNumber);
        return count>0;
    }

    private Volume Map(ResultSet resultSet) throws SQLException{
        Long id = resultSet.getLong("id");
        String title = resultSet.getString("title");
        int volume_main_number = resultSet.getInt("volume_main_number");
        int volume_sub_number = resultSet.getInt("volume_sub_number");
        Long bookId = resultSet.getLong("book_id");
        boolean isDefault = resultSet.getBoolean("is_default");
        return new Volume(id, title, volume_main_number, volume_sub_number, bookId, isDefault);
    }

}
