package com.university.hotel.repos;

import com.university.hotel.entities.Report;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;

public interface ReportRepo extends CrudRepository<Report, Long>{
    @Query(value = "SELECT r FROM Report r WHERE(r.dateFrom=?1 AND r.dateTo=?2)")
    Report findByDateFromEqualsAndDateToEquals(Date dateFrom, Date dateTo);
}
