package indi.zhuyst.base.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类，包含表共有字段
 * @author zhuyst
 */
@Data
public class BaseDO implements Serializable {

    /**
     * 唯一标识ID
     */
    @Id
    @ApiModelProperty("唯一标识ID")
    protected String id;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm")
    private Date createDate;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm")
    private Date modifiedDate;
}
