package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.NewCommentRequest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", source = "user")
    @Mapping(target = "created", expression = "java(LocalDateTime.now())")
    Comment mapToComment(NewCommentRequest request, User user, Item item);

    @Mapping(target = "itemId", source = "comment.item.id")
    @Mapping(target = "authorName", source = "comment.author.name")
    CommentDto mapToCommentDto(Comment comment);
}
