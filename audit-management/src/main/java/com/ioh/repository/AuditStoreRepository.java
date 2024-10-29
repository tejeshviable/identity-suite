package com.ioh.repository;


import com.ioh.dto.AuditStoreDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditStoreRepository extends MongoRepository<AuditStoreDto, String> {




    List<AuditStoreDto> findByMessageInOrderByMessageAsc(List<String> message);


}
