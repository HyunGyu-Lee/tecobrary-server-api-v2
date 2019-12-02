package com.woowacourse.tecobrary.librarybook.domain;

import com.woowacourse.tecobrary.common.model.BookCoverUrl;
import com.woowacourse.tecobrary.common.model.BookInfo;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "LibraryBooks")
@Access(AccessType.FIELD)
public class LibraryBook {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Embedded
    private BookCoverUrl libraryBookCoverUrl;

    @Embedded
    private BookInfo libraryBookInfo;

    public LibraryBook(BookCoverUrl libraryBookCoverUrl, BookInfo libraryBookInfo) {
        this.libraryBookCoverUrl = libraryBookCoverUrl;
        this.libraryBookInfo = libraryBookInfo;
    }

    public String getTitle() {
        return libraryBookInfo.getTitle();
    }

    public String getAuthor() {
        return libraryBookInfo.getAuthor();
    }

    public String getPublisher() {
        return libraryBookInfo.getPublisher();
    }

    public String getIsbn() {
        return libraryBookInfo.getIsbn();
    }

    public String getDescription() {
        return libraryBookInfo.getDescription();
    }

    public String getImage() {
        return libraryBookCoverUrl.getImage();
    }
}
