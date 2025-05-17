package com.example.demo.repo;

import com.example.demo.model.RedirectCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RedirectCountRepository extends JpaRepository<RedirectCount, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE redirect_count SET count = count + 1 WHERE url_mapping_id = :id", nativeQuery = true)
    void incrementCount(@Param("id") Long id);

}
