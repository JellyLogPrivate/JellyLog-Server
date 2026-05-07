package com.saram.jellylog.point.repository;

import com.saram.jellylog.point.entity.Point;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;

public interface PointRepository extends JpaRepository<Point, Long> {

    List<Point> findByUserCode(Long userCode, Sort sort);
}

