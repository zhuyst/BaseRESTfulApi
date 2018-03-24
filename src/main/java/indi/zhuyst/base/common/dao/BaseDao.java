package indi.zhuyst.base.common.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 基础DAO，通用Mapper使用
 * @param <T> 操作实体类
 * @author zhuyst
 */
@NoRepositoryBean
public interface BaseDao<T> extends MongoRepository<T,String> {
}
