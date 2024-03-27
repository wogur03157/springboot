package com.example.demo.domain.mogoTest;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface freightPositionRepository extends MongoRepository<freightPositionDto, String> {

}
