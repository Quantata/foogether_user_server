package foogether.users.repository;

import foogether.users.domain.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByIdx(int userIdx);
    User findByPhoneNum(String phoneNum);
    User findByPhoneNumAndPassword(String phoneNum, String password);

//    List<User> findAllByIdx(int userIdx);

}
