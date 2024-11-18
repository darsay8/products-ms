package dev.rm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dev.rm.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
