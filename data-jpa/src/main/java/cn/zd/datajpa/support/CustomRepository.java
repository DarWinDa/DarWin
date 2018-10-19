package cn.zd.datajpa.support;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * 继承 JpaRepository，具备了 JpaRepository 所提供的方法，继承了 JpaSpecificationExecutor，具备使用 Specification 的能力
 * @author: Zhao Da
 * @since: 2018/9/7 09:18
 */
@NoRepositoryBean
public interface CustomRepository<T, ID extends Serializable>extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    Page<T> findByAuto(T example, Pageable pageable);
}
