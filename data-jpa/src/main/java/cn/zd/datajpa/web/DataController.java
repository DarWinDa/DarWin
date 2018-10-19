package cn.zd.datajpa.web;

import cn.zd.datajpa.dao.PersonRepository;
import cn.zd.datajpa.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据库操作试用控制器
 * @author: Zhao Da
 * @since: 2018/9/6 09:15
 */
@RestController
public class DataController {
    @Autowired
    PersonRepository personRepository;

    @RequestMapping("/sava")
    public Person save(String name, String address, Integer age) {
        Person p = personRepository.save(new Person(null, name, age, address));

        return p;
    }

    @RequestMapping("/q1")
    public List<Person> q1(String address) {
        List<Person> people = personRepository.findByAddress(address);
        return people;
    }

    @RequestMapping("/q2")
    public Person q2(String name, String address) {
        Person people = personRepository.findByNameAndAddress(name, address);
        return people;
    }

    @RequestMapping("/q3")
    public Person q3(String name, String address) {
        Person people = personRepository.withNameAndAddressQuery(name, address);
        return people;
    }

    @RequestMapping("/q4")
    public Person q4(String name, String address) {
        Person people = personRepository.withNameAndAddressNamedQuery(name, address);
        return people;
    }

    @RequestMapping("/sort")
    public List<Person> sort() {
        List<Person> people = personRepository.findAll(new Sort(Sort.Direction.ASC, "age"));
        return people;
    }

    @RequestMapping("/page")
    public Page<Person> page() {
        Page<Person> page = personRepository.findAll(new PageRequest(1, 2));
        return page;
    }
}
