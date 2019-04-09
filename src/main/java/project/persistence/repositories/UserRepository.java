package project.persistence.repositories;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.persistence.entities.User;

import java.util.List;

/**
 * By extending the {@link JpaRepository} we have access to powerful methods.
 * For detailed information, see:
 * http://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html
 * http://docs.spring.io/spring-data/data-commons/docs/1.6.1.RELEASE/reference/html/repositories.html
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User save(User user);

    void delete(User user);

    List<User> findAll();

    // If we need a custom query that maybe doesn't fit the naming convention used by the JPA repository,
    // then we can write it quite easily with the @Query notation, like you see below.
    // This method returns all Users where the length of the name is equal or greater than 3 characters.
    @Query(value = "SELECT u FROM User u where length(u.username) >= 3 ")
    List<User> findAllWithNameLongerThan3Chars();

    // Instead of the method findAllReverseOrder() in User.java,
    // We could have used this method by adding the words
    // ByOrderByIdDesc, which mean: Order By Id in a Descending order
    //
    List<User> findAllByOrderByIdDesc();

    @Query(value = "SELECT u FROM User u WHERE u.id = ?1")
    User findOne(Long id);

    User findByUsername(String username);


    @Query(value= "SELECT u.username, u.gameScore FROM User u")
    List<Object> getAllGameScores();

}
