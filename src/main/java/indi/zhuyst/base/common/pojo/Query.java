package indi.zhuyst.base.common.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 查询对象
 * @author zhuyst
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Query<E> implements Serializable{

    private static final long serialVersionUID = 3616619501307355095L;

    /**
     * 页码
     */
    @ApiModelProperty("页码")
    private Integer pageNum;

    /**
     * 页面大小
     */
    @ApiModelProperty("页面大小")
    private Integer pageSize;

    @ApiModelProperty("排序的属性名")
    private String sortProperty;

    @ApiModelProperty("排序的方向：ASC|DESC")
    private String sortDirection;

    /**
     * 查询对象（等号查询）
     */
    @ApiModelProperty(hidden = true)
    private E entity;

    /**
     * 通过非泛型的Query对象转为泛型的Query对象
     * @param query 非泛型的Query对象
     */
    public Query(Query query){
        BeanUtils.copyProperties(query,this,"entity");
    }

    /**
     * 通过非泛型的Query对象转为泛型的Query对象
     * @param query 非泛型的Query对象
     * @param entity 要设置的{@link #entity}
     */
    public Query(Query query,E entity){
        BeanUtils.copyProperties(query,this,"entity");
        this.entity = entity;
    }
}
