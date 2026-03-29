package ru.anastasya.readingportal.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CRUDutil {

    public static <T> List<T> readMany(String sql, RowMapper<T> mapper, Object... params){

        List<T> objs = read(sql, rs -> {
            List<T> list = new ArrayList<>();
            while (rs.next()){
                list.add(mapper.map(rs));
            }
            return list;
                }, params);
        return objs;
    }

    public static <T> T readOne(String sql, RowMapper<T> mapper, Object... params){

        T obj = read(sql, resultSet -> {
            if (resultSet.next()){
                return mapper.map(resultSet);
            }
            return null;
        }, params);

        return obj;
    }

    private static <T> T read(String sql, ResultSetHandler<T> resultSetHandler, Object... params){

        try(Connection connection = DBConnector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i=1; i<=params.length; i++){
                preparedStatement.setObject(i, params[i-1]);
            }

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                return resultSetHandler.handle(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Long insert(String sql, Object... params){

        try(Connection connection = DBConnector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (int i=1; i<=params.length; i++){
                preparedStatement.setObject(i, params[i-1]);
            }

            preparedStatement.executeUpdate();

            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                resultSet.next();
                return resultSet.getLong(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int update(String sql, Object... params){

        try(Connection connection = DBConnector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i=1; i<=params.length; i++){
                preparedStatement.setObject(i, params[i-1]);
            }

            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static <K, T> HashMap<K, List<T>> readHashMapKeyAndObjects(String sql, String keyName, Class<K> type, RowMapper<T> mapper, Object... params){
        HashMap<K, List<T>> map = new HashMap<>();
        try(Connection connection = DBConnector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i=1; i <= params.length; i++){
                preparedStatement.setObject(i, params[i-1]);
            }

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                K key = rs.getObject(keyName, type);
                T object = mapper.map(rs);

                List<T> list;
                if (map.containsKey(key)){
                    list = map.get(key);
                }
                else{
                    list = new ArrayList<>();
                }
                list.add(object);
                map.put(key, list);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return map;
    }



}
