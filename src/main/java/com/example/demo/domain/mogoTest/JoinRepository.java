package com.example.demo.domain.mogoTest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
//public interface JoinRepository extends JpaRepository<JoinDto, String> {
//    @Query(value = "SELECT i.*, r.* FROM container_in_out i JOIN container_io_result r ON i.ctio_seq = r.copion_seq", nativeQuery = true)
//    List<JoinDto> getJoinedData();
//}
