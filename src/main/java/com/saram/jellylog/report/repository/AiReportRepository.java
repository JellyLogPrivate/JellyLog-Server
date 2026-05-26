package com.saram.jellylog.report.repository;

import com.saram.jellylog.report.entity.AiReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AiReportRepository extends JpaRepository<AiReport, Long> {
    List<AiReport> findByUserCode(Long userCode);
    Optional<AiReport> findByUserCodeAndYearMonth(Long userCode, String yearMonth);
}
