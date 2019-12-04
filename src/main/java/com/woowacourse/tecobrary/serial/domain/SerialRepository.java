package com.woowacourse.tecobrary.serial.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SerialRepository extends JpaRepository<Serial, Long> {

    boolean existsBySerialInfoSerialNumber(Long serialNumber);

    List<Serial> findAllBySerialLibraryBookBookId(Long bookId);
}
