package cn.zd.datajpa.specs;

import static com.google.common.collect.Iterables.toArray;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * Specification Repository
 * @author: Zhao Da
 * @since: 2018/9/6 11:21
 */
public class CustomerSpecs {

    /**
     * 定义一个返回值为 Specification 的方法 byAuto，这里使用的是泛型 T, 所以这个 Specification 是可以用于任意的实体类的。它接受的参数是 entityManager
     * 和当前的包含值作为查询条件的实体类对象
     * @param entityManager entityManager
     * @param example 任意实体类
     * @param <T> 任意实体类
     * @return
     */
    public static <T> Specification<T> byAuto(final EntityManager entityManager, final T example) {
        final Class<T> type = (Class<T>) example.getClass();// 获得当前实体类对象类的类型

        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();// 新建 Predicate 列表存储构造的查询条件

                EntityType<T> entityType = entityManager.getMetamodel().entity(type);// 获得实体类的 EntityType，我们可以从 EntityType 获得实体类的属性

                for (Attribute<T, ?> attr : entityType.getDeclaredAttributes()) {// 对实体类的所有属性做循环
                    Object attrValue = getValue(example, attr);// 获得实体类对象某一个属性的值
                    if (attrValue != null) {
                        if (attr.getJavaType() == String.class) {// 当前属性值为字符类型的时候
                            if (!StringUtils.isEmpty(attrValue)) {// 若当前字符不为空的情况下
                                // 构造当前属性 like（前后 %）属性值查询条件，并添加到条件列表中
                                predicates.add(criteriaBuilder.like(root.get(attribute(entityType, attr.getName(), String.class)),pattern((String) attrValue)));
                            }
                        } else {
                            // 其余情况下，构造属性和属性值 equal 查询条件，并添加到条件列表中
                            predicates.add(criteriaBuilder.equal(root.get(attribute(entityType, attr.getName(), attrValue.getClass())), attrValue));
                        }
                    }
                }
                // 将条件列表转换成 Predicate
                return predicates.isEmpty() ? criteriaBuilder.conjunction() : criteriaBuilder.and(toArray(predicates, Predicate.class));
            }

            /**
             * 通过反射获得实体类对象对应属性的属性值
             * @param example 实体类
             * @param attr 属性
             * @param <T> 属性值
             * @return
             */
            private <T> Object getValue(T example, Attribute<T, ?> attr) {
                return ReflectionUtils.getField((Field) attr.getJavaMember(), example);
            }

            /**
             * 获得实体类的当前属性的 SingularAttribute， SingularAttribute 包含的是实体类的某个单独属性
             * @param entity 查询实体类
             * @param fieldName 属性名
             * @param fieldClass 属性的 Class
             * @param <E> 属性类型泛型
             * @param <T> 实体类泛型
             * @return
             */
            private <E, T>SingularAttribute<T, E> attribute(EntityType<T> entity, String fieldName, Class<E> fieldClass) {
                return entity.getDeclaredSingularAttribute(fieldName, fieldClass);
            }
        };
    }

    /**
     * 构造 like 的查询模式，即前后加 %
     * @param str 要查询的字符串
     * @return
     */
    static private String pattern(String str) {
        return "%" + str + "%";
    }
}
