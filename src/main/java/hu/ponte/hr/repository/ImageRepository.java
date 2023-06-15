package hu.ponte.hr.repository;

import hu.ponte.hr.domain.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, String> {

    @Query("SELECT i FROM ImageEntity i WHERE i.name like :name")
    Optional<ImageEntity> findByName(@Param("name") String name);
}
