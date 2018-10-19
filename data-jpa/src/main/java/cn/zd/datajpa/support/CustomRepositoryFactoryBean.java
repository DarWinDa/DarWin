package cn.zd.datajpa.support;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * 自定义 JpaRepositoryFactoryBean 替代默认 RepositoryFactoryBean,我们会获得一个 RepositoryFactory，RepositoryFactory 将会注册我们自定义的 Repository 的实现
 * @author: Zhao Da
 * @since: 2018/9/7 09:29
 */
public class CustomRepositoryFactoryBean<T extends JpaRepository<S, ID>, S, ID
        extends Serializable> extends JpaRepositoryFactoryBean<T, S, ID> {

    public CustomRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    } // 自定义 RepositoryFactoryBean，继承 JpaRepositoryFactoryBean

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new CustomRepositoryFactory(entityManager);
    }

    private static class CustomRepositoryFactory extends JpaRepositoryFactory {
        public CustomRepositoryFactory(EntityManager entityManager) {
            super(entityManager);
        }

        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return CustomRepositoryImpl.class;
        }

        /*@Override
        @SuppressWarnings({"unchecked"})
        protected <T, ID extends Serializable>SimpleJpaRepository<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
            return new CustomRepositoryImpl<T, ID>((Class<T>)information.getDomainType(), entityManager);
        }*/
        @Override
        protected SimpleJpaRepository<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
            return new CustomRepositoryImpl<>((Class<Object>)information.getDomainType(), entityManager);
        }
    }
}
