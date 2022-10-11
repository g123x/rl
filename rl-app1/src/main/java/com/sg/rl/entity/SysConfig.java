package com.sg.rl.entity;


import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 参数配置表 sys_config
 * 
 * @author ruoyi
 */

@Data
public class SysConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long configId;

    private String configName;

    private String configKey;

    private String configValue;

    private String configType;


}
