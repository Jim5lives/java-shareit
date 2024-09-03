package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.error.exceptions.AccessForbiddenException;
import ru.practicum.shareit.error.exceptions.BadRequestException;
import ru.practicum.shareit.error.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto createItem(Integer userId, NewItemRequest request) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id=" + userId));
        Item item = ItemMapper.mapToItem(request);
        item.setOwnerId(owner.getId());

        item = itemRepository.save(item);
        log.info("Вещь успешно создана: {}", item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemCommentDto findItemById(Integer userId, Integer id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не найдена вещь с id=" + id));
        BookingDto lastBooking = null;
        BookingDto nextBooking = null;
        if (item.getOwnerId().equals(userId)) {
            List<BookingDto> bookings = bookingRepository.findByItemId(id).stream()
                    .map(BookingMapper::mapToBookingDto)
                    .toList();
            lastBooking = findLast(bookings);
            nextBooking = findNext(bookings);
        }
        log.info("Вещь найдена по id={}: {}", id, item);
        return ItemMapper.mapToItemAllDto(item, lastBooking, nextBooking, findComments(id));
    }

    @Override
    @Transactional
    public ItemDto updateItem(Integer userId, Integer itemId, UpdateItemRequest request) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Не найдена вещь с id=" + itemId));

        if (isOwnerValid(userId, itemId)) {
            log.trace("Валидация пройдена userId={} и itemId={} совпадают", userId, itemId);
            Item itemToUpdate = updateItemFields(item, request);
            Item updatedItem = itemRepository.save(itemToUpdate);
            log.info("Вещь успешно обновлена: {}", updatedItem);
            return ItemMapper.mapToItemDto(updatedItem);

        } else {
            log.warn("Не совпадают userId={} и ownerId={} вещи для обновления", userId, item.getOwnerId());
            throw new AccessForbiddenException("Нет прав для редактирования вещи у пользователя с id=" + userId);
        }
    }

    @Override
    @Transactional
    public void deleteItem(Integer userId, Integer itemId) {
        if (isOwnerValid(userId, itemId)) {
            log.trace("Валидация успешна userId={} и itemId={} совпадают", userId, itemId);
            itemRepository.deleteById(itemId);

        } else {
            log.warn("Не совпадают userId={} и ownerId={} вещи для удаления", userId, itemId);
            throw new AccessForbiddenException("Нет прав для удаления вещи у пользователя с id=" + userId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemCommentDto> getAllUsersItems(Integer userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id=" + userId));

        Map<Integer, List<BookingDto>> bookingsMap = bookingRepository.findByItemOwnerIdOrderByStartDesc(userId)
                .stream()
                .map(BookingMapper::mapToBookingDto)
                .collect(Collectors.groupingBy(booking -> booking.getItem().getId()));

        log.info("Выводится список вещей пользователя с id={}", owner.getId());
        return itemRepository.findAllByOwnerId(userId).stream()
                .map(item -> {
                    List<BookingDto> itemBookings = bookingsMap.getOrDefault(item.getId(), Collections.emptyList());
                    return ItemMapper.mapToItemAllDto(
                            item,
                            findLast(itemBookings),
                            findNext(itemBookings),
                            findComments(item.getId())
                    );
                }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> search(String query) {
        if (query.isBlank()) {
            log.info("Передана пустая строка для поиска");
            return List.of();
        }

        log.info("Выводится список вещей, содержащих '{}'", query);
        return itemRepository.search(query).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    @Transactional
    public CommentDto addComment(Integer userId, Integer itemId, NewCommentRequest request) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Не найдена вещь с id=" + itemId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id=" + userId));

        if (bookingRepository.findAllByBookerIdAndItemIdAndStatusAndEndBefore(userId, itemId,
                BookingStatus.APPROVED, LocalDateTime.now()).isEmpty()) {
            throw new BadRequestException("Нет прав для добавления комментария");
        }

        Comment comment = commentRepository.save(CommentMapper.mapToComment(request, user, item));
        log.info("Комментарий {} успешно добавлен", request.getText());
        return CommentMapper.mapToCommentDto(comment);
    }

    private List<CommentDto> findComments(Integer itemId) {
        return commentRepository.findAllByItemIdOrderByCreated(itemId).stream()
                .map(CommentMapper::mapToCommentDto)
                .toList();
    }

    private boolean isOwnerValid(Integer userId, Integer itemId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id=" + userId));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Не найдена вещь с id=" + itemId));

        return item.getOwnerId().equals(owner.getId());
    }

    private Item updateItemFields(Item item, UpdateItemRequest request) {
        if (request.hasName()) {
            item.setName(request.getName());
        }
        if (request.hasDescription()) {
            item.setDescription(request.getDescription());
        }
        if (request.hasAvailable()) {
            item.setAvailable(request.getAvailable());
        }
        return item;
    }

    private BookingDto findLast(List<BookingDto> booking) {
        if (booking == null) {
            return null;
        } else {
            return booking.stream()
                    .filter(bk -> bk.getEnd().isBefore(LocalDateTime.now()))
                    .max(Comparator.comparing(BookingDto::getEnd))
                    .orElse(null);
        }
    }

    private BookingDto findNext(List<BookingDto> booking) {
        if (booking == null) {
            return null;
        } else {
            return booking.stream()
                    .filter(bk -> bk.getStart().isAfter(LocalDateTime.now()))
                    .min(Comparator.comparing(BookingDto::getEnd))
                    .orElse(null);
        }
    }

}
