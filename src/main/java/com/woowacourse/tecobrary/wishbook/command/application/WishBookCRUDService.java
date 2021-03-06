package com.woowacourse.tecobrary.wishbook.command.application;

import com.woowacourse.tecobrary.common.application.SlackBotService;
import com.woowacourse.tecobrary.wishbook.command.domain.WishBook;
import com.woowacourse.tecobrary.wishbook.command.util.WishBookInfoDtoMapper;
import com.woowacourse.tecobrary.wishbook.command.util.WishBookMapper;
import com.woowacourse.tecobrary.wishbook.ui.dto.WishBookInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class WishBookCRUDService {

    private WishBookService wishBookService;
    private SlackBotService slackBotService;

    @Autowired
    public WishBookCRUDService(final WishBookService wishBookService, final SlackBotService slackBotService) {
        this.wishBookService = wishBookService;
        this.slackBotService = slackBotService;
    }

    public WishBookInfoDto findById(final Long id) {
        return WishBookInfoDtoMapper.toDto(wishBookService.findById(id));
    }

    public List<WishBookInfoDto> findWishBooksOnPage(final int page, final int number) {
        return wishBookService.findWishBooksOnPage(page, number)
                .stream()
                .map(WishBookInfoDtoMapper::toDto)
                .collect(toList());
    }

    public Long createWishBook(final WishBookInfoDto wishBookInfoDto) {
        WishBook wishBook = wishBookService.createWishBook(wishBookInfoDto);
        slackBotService.wishBookNotification(WishBookMapper.toDto(wishBook));
        return wishBook.getId();
    }

    public WishBookInfoDto softDeleteById(final Long id) {
        return WishBookInfoDtoMapper.toDto(wishBookService.softDeleteById(id));
    }

    public void deleteWishBook(final Long id) {
        wishBookService.deleteWishBook(id);
    }
}
