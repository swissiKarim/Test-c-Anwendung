//package com.backend.devp.repository;
//
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//import org.springframework.data.repository.query.Param;
//import com.backend.devp.model.User;
//import java.util.List;
//
//
//public interface UserRepository extends JpaRepository<User, Long>{
//
//	
//	 Optional<User> findByUsername(String username);
//	 
//	 @Query("Select u.name from User u where u.id in (:pIdList)")
//	    List<String> findUserIdList(@Param("pIdList") List<Long> idList);
//}
