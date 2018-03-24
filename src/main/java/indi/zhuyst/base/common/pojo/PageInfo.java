package indi.zhuyst.base.common.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

/**
 * 分页对象
 * @param <E> List中存储的对象类型
 * @author zhuyst
 */
@Data
@NoArgsConstructor
public class PageInfo<E> implements Serializable{

    /**
     * 列表
     */
    @ApiModelProperty("列表")
    private List<E> list;

    /**
     * 总数
     */
    @ApiModelProperty("总数")
    private Long total;

    /**
     * 总页数
     */
    @ApiModelProperty("总页数")
    private Integer pages;

    /**
     * 查询时的页码
     */
    @ApiModelProperty("查询时的页码")
    private Integer pageNum;

    /**
     * 查询时的页面大小
     */
    @ApiModelProperty("查询时的页面大小")
    private Integer pageSize;

    public PageInfo(Page<E> page){
        this.list = page.getContent();
        this.total = page.getTotalElements();
        this.pages = page.getTotalPages();
        this.pageNum = page.getNumber();
        this.pageSize = page.getSize();
    }
}
