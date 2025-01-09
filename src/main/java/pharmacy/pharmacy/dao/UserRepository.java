package pharmacy.pharmacy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pharmacy.pharmacy.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email); // Custom query to find user by email
}
