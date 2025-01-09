package com.book_store.repository;

import com.book_store.entity.Notify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface NotifyRepository extends JpaRepository<Notify, Integer> {
    List<Notify> findByCustomerId(Integer customerId);

    @Query("SELECT n FROM Notify n WHERE n.customerId IS NULL")
    List<Notify> findAllByCustomerIdIsNull();

}
