package indi.zhuyst.base.common.service.impl;

import indi.zhuyst.base.common.dao.BaseDao;
import indi.zhuyst.base.common.entity.BaseDO;
import indi.zhuyst.base.common.enums.CodeEnum;
import indi.zhuyst.base.common.exception.CommonException;
import indi.zhuyst.base.common.pojo.PageInfo;
import indi.zhuyst.base.common.pojo.Query;
import indi.zhuyst.base.common.service.BaseCrudService;
import indi.zhuyst.base.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * BaseCrudService的实现类
 * @param <D> 操作对应实体的DAO
 * @param <E> 操作的实体类
 * @author zhuyst
 */
public abstract class BaseCrudServiceImpl<D extends BaseDao<E>,E extends BaseDO>
        extends BaseService implements BaseCrudService<E> {

    /**
     * 默认页面大小
     */
    private static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 操作DAO
     */
    @Autowired
    protected D dao;

    @Override
    public E getByID(String id){
        return dao.findOne(id);
    }

    @Override
    public PageInfo<E> listByCondition(Query<E> query){

        // 如果没有指定页面大小，设定一个默认值
        if(query.getPageSize() == null){
            query.setPageSize(DEFAULT_PAGE_SIZE);
        }

        // 创建查询对象
        Example<E> example = null;
        E entity = query.getEntity();
        if(entity != null){
            example = Example.of(entity);
        }

        // 对排序进行处理
        Sort sort = null;
        String sortProperty = query.getSortProperty();
        if(sortProperty != null){
            Sort.Direction direction = Sort.Direction.fromStringOrNull(sortProperty);
            Sort.Order order = new Sort.Order(direction,sortProperty);
            sort = new Sort(order);
        }

        // 创建分页排序查询对象
        PageRequest pageRequest = new PageRequest(query.getPageNum(),
                query.getPageSize(),sort);

        // 进行查询
        Page<E> page = example == null ? dao.findAll(pageRequest) :
                dao.findAll(example,pageRequest);
        return new PageInfo<>(page);
    }

    @Override
    public List<E> listAll(){
        return dao.findAll();
    }

    @Override
    public long countAll(){
        return dao.count();
    }

    @Override
    public long countByCondition(Query<E> query) {
        Example<E> example = Example.of(query.getEntity());
        return dao.count(example);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public E save(E entity){
        Date date = new Date();

        // 如果ID为NULL，执行INSERT操作
        String id = entity.getId();
        if(id == null){
            entity.setCreateDate(date);
            entity.setModifiedDate(date);
            dao.insert(entity);
        }

        // 反则执行UPDATE操作
        else {
            checkExists(id);

            entity.setModifiedDate(date);
            dao.save(entity);
        }

        // 如果成功则访问对象，反则访问NULL
        return this.getByID(entity.getId());
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delete(String id){
        dao.delete(id);
    }

    private void checkExists(String id){
        if(getByID(id) == null){
            throw new CommonException(CodeEnum.NOT_FOUND);
        }
    }
}
