package com.microservices.profileuser.repository;

import com.microservices.profileuser.entity.UserProfile;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends Neo4jRepository <UserProfile , String>  {
}
