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
            INSERT INTO volumes(title, volume_main_number, volume_sub_number, book_id)
            VALUES (?, ?, ?, ?);""";
    private static final String DELETE_VOLUME_SQL = "DELETE FROM volumes WHERE id = ?;";
    private static final String FIND_LAST_MAIN_VOLUME_NUMBER_SQL = """
            SELECT MAX(volume_main_number)
            FROM volumes
            WHERE book_id = ?;""";
    private static final String FIND_VOLUME_BY_ID_SQL = "SELECT * FROM volumes WHERE id = ?;";
    private static final String FIND_ALL_VOLUMES_BY_BOOK_ID_SQL = "SELECT * FROM volumes WHERE book_id = ?;";

    public void save(Volume volume){
        Objects.requireNonNull(volume, "Нельзя сохранить null volume");
        CRUDutil.insert(SAVE_VOLUME_SQL, volume.getTitle(), volume.getVolume_main_number(),
                volume.getVolume_sub_number(), volume.getBook_id());
    }

    public void update(Volume volume){
        Objects.requireNonNull(volume, "Нельзя изменить null volume");
        CRUDutil.update(UPDATE_VOLUME_SQL, volume.getTitle(), volume.getVolume_main_number(),
                volume.getVolume_sub_number(), volume.getId());
    }

    public void delete(Long volumeId){
        CRUDutil.update(DELETE_VOLUME_SQL, volumeId);
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

    private Volume Map(ResultSet resultSet) throws SQLException{
        Long id = resultSet.getLong("id");
        String title = resultSet.getString("title");
        int volume_main_number = resultSet.getInt("volume_main_number");
        int volume_sub_number = resultSet.getInt("volume_sub_number");
        Long book_id = resultSet.getLong("book_id");
        return new Volume(id, title, volume_main_number, volume_sub_number, book_id);
    }

}
