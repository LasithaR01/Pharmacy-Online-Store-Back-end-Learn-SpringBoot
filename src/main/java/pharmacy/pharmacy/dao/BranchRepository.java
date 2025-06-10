package pharmacy.pharmacy.dao;

import pharmacy.pharmacy.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Integer> {
}