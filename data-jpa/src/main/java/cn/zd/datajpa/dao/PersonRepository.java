package cn.zd.datajpa.dao;

import cn.zd.datajpa.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author: Zhao Da
 * @since: 2018/9/6 09:03
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByAddress(String name);

    Person findByNameAndAddress(String name, String address);

    @Query("select p from Person p where p.name= :name and p.address= :address")
    Person withNameAndAddressQuery(@Param("name")String name, @Param("address")String address);

    // 使用@NamedQuery查询，请注意我们在实体类中做的@NamedQuery的定义
    Person withNameAndAddressNamedQuery(String name, String address);
}
