package ru.yandex.practicum.catsgram.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.catsgram.model.Post;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostRowMapper implements RowMapper<Post> {
    @Override
    public Post mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Post post = new Post();
        post.setId(resultSet.getLong("id"));
        post.setAuthorId(resultSet.getLong("authorId"));
        post.setDescription(resultSet.getString("description"));
        post.setPostDate(resultSet.getTimestamp("post_date").toInstant());

        return post;
    }
}
