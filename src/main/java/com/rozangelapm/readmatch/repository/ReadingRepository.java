package com.rozangelapm.readmatch.repository;

import com.rozangelapm.readmatch.model.Reading;
import com.rozangelapm.readmatch.model.ReadingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReadingRepository extends JpaRepository<Reading, Long> {

    Optional<Reading> findByBook_Id(String bookId);

    List<Reading> findByStatus(ReadingStatus status);
}
