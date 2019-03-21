package project.persistence.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.persistence.entities.Beer;
import project.persistence.entities.Drinklist;

import java.util.List;

@Repository
public interface DrinklistRepository extends JpaRepository<Drinklist, Long> {

    Drinklist save(Drinklist drinklist);

    void delete(Drinklist drinklist);

    Drinklist findByDrinklistId(Long id);

    @Query(value = "SELECT d FROM Drinklist d WHERE d.isPublic = true")
    List<Drinklist> findPublicDrinklists();
}
