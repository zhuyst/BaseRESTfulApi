package indi.zhuyst.base.common.util;

import indi.zhuyst.base.common.pojo.PageInfo;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class PageUtils {

    public static <T> PageInfo<T> copyNewPageInfo(PageInfo pageInfo,List<T> list){
        PageInfo<T> newPageInfo = new PageInfo<>();

        newPageInfo.setList(list);
        BeanUtils.copyProperties(pageInfo,newPageInfo,"list");

        return newPageInfo;
    }
}
