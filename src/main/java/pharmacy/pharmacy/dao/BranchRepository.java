package pharmacy.pharmacy.dao;

import pharmacy.pharmacy.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Integer> {

    // Check if a branch exists with the given name (case-insensitive)
    boolean existsByNameIgnoreCase(String name);

    // Check if a branch has associated stock records
    @Query("SELECT COUNT(s) > 0 FROM Stock s WHERE s.branch.id = :branchId")
    boolean hasAssociatedStock(@Param("branchId") int branchId);

    // Find branch by name (exact match)
    Optional<Branch> findByName(String name);

    // Find branch by name containing (case-insensitive)
    @Query("SELECT b FROM Branch b WHERE LOWER(b.name) LIKE LOWER(concat('%', :name, '%'))")
    List<Branch> findByNameContainingIgnoreCase(@Param("name") String name);

    // Find branches by location
    List<Branch> findByLocation(String location);

    // Find branches by location containing (case-insensitive)
    @Query("SELECT b FROM Branch b WHERE LOWER(b.location) LIKE LOWER(concat('%', :location, '%'))")
    List<Branch> findByLocationContainingIgnoreCase(@Param("location") String location);

    // Check if another branch with the same name exists (excluding current branch)
    @Query("SELECT COUNT(b) > 0 FROM Branch b WHERE LOWER(b.name) = LOWER(:name) AND b.id != :excludeId")
    boolean existsByNameAndIdNot(@Param("name") String name, @Param("excludeId") int excludeId);

    boolean existsByName(String name);
}