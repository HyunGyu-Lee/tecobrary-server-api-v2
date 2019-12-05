package com.woowacourse.tecobrary.wishbook.command.application;

import com.woowacourse.tecobrary.wishbook.command.domain.DuplicatedWishBookIsbnException;
import com.woowacourse.tecobrary.wishbook.command.domain.NotFoundWishBookException;
import com.woowacourse.tecobrary.wishbook.command.domain.WishBook;
import com.woowacourse.tecobrary.wishbook.command.domain.WishBookRepository;
import com.woowacourse.tecobrary.wishbook.command.util.WishBookInfoDtoMapper;
import com.woowacourse.tecobrary.wishbook.ui.dto.WishBookInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class WishBookService {

    private final WishBookRepository wishBookRepository;

    @Autowired
    public WishBookService(final WishBookRepository wishBookRepository) {
        this.wishBookRepository = wishBookRepository;
    }

    public List<WishBookInfoDto> findWishBooksOnPage(final int page, final int number) {
        Page<WishBook> pageWishBooks = wishBookRepository.findAllByDeletedAtNull(PageRequest.of(page - 1, number));

        return pageWishBooks.getContent()
                .stream()
                .map(WishBookInfoDtoMapper::toDto)
                .collect(toList());
    }

    @Transactional
    public Long createWishBook(final WishBookInfoDto wishBookInfoDto) {
        checkDuplicatedWishNotEnrolledLibraryBookIsbn(wishBookInfoDto);

        WishBook wishBook = WishBookInfoDtoMapper.toEntity(wishBookInfoDto);
        return wishBookRepository.save(wishBook).getId();
    }

    private void checkDuplicatedWishNotEnrolledLibraryBookIsbn(final WishBookInfoDto wishBookInfoDto) {
        if (wishBookRepository.existsByWishBookInfoIsbnAndDeletedAtNull(wishBookInfoDto.getIsbn())) {
            throw new DuplicatedWishBookIsbnException();
        }
    }

    public WishBookInfoDto findById(final Long id) {
        WishBook wishBook = getWishBook(id);
        return WishBookInfoDtoMapper.toDto(wishBook);
    }

    private WishBook getWishBook(final Long id) {
        return wishBookRepository.findById(id)
                .orElseThrow(NotFoundWishBookException::new);
    }

    public WishBook findNotEnrolledById(final Long id) {
        return findByIdNotSoftDeleted(id)
                .orElseThrow(NotFoundWishBookException::new);
    }

    private Optional<WishBook> findByIdNotSoftDeleted(final Long id) {
        return wishBookRepository.findByIdAndDeletedAtNull(id);
    }

    public WishBook findEnrolledById(final Long id) {
        return findByIdSoftDeleted(id)
                .orElseThrow(NotFoundWishBookException::new);
    }

    private Optional<WishBook> findByIdSoftDeleted(final Long id) {
        return wishBookRepository.findByIdAndDeletedAtNotNull(id);
    }

    @Transactional
    public WishBookInfoDto softDeleteById(final Long id) {
        checkNotSoftDeleted(id);
        return WishBookInfoDtoMapper.toDto(softDelete(id));
    }

    private void checkNotSoftDeleted(final Long id) {
        if (!existsByIdAndSoftDeleted(id)) {
            throw new AlreadySoftDeletedWishBookException();
        }
    }

    private boolean existsByIdAndSoftDeleted(final Long id) {
        return wishBookRepository.existsByIdAndDeletedAtNotNull(id);
    }

    private WishBook softDelete(final Long id) {
        WishBook wishBook = getWishBook(id);
        wishBook.softDelete();
        return wishBook;
    }

    @Transactional
    public void deleteWishBook(final Long id) {
        checkNotExistWishBook(id);
        wishBookRepository.deleteById(id);
    }

    private void checkNotExistWishBook(final Long id) {
        if (!wishBookRepository.existsById(id)) {
            throw new NotFoundWishBookException();
        }
    }
}
