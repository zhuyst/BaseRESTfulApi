package indi.zhuyst.base.common.util;

import indi.zhuyst.base.common.pojo.PageInfo;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * 分页工具类
 * @see PageInfo
 * @author zhuyst
 */
public class PageUtils {

    /**
     * 将分页对象信息拷贝到新对象中
     * @param pageInfo 旧分页对象
     * @param list 要拷贝的新分页对象的list
     * @param <T> 新对象类型
     * @return 新的分页对象
     */
    public static <T> PageInfo<T> copyNewPageInfo(PageInfo pageInfo,List<T> list){
        PageInfo<T> newPageInfo = new PageInfo<>();

        newPageInfo.setList(list);
        BeanUtils.copyProperties(pageInfo,newPageInfo,"list");

        return newPageInfo;
    }
}
