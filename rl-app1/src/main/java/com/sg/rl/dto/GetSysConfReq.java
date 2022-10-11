package com.sg.rl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "获取金主frp端口请求体")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetSysConfReq {
    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("渠道号 1.支付宝 2.微信 3.银行卡 4.云闪付")
    private Integer channel;//1.支付宝 2.微信 3.银行卡 4.云闪付
    @ApiModelProperty("账号名如：支付宝账号 微信账号 银行卡号等")
    private String account;
}
