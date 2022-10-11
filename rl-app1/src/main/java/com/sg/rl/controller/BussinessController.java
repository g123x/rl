package com.sg.rl.controller;


import com.alibaba.fastjson.JSON;
import com.sg.rl.common.exception.ServiceException;
import com.sg.rl.dto.GetSysConfReq;
import com.sg.rl.entity.SysConfig;
import com.sg.rl.service.IBussinessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.sg.rl.common.constants.ExceptionDefineEnum.NO_PERMITION;

@Api(tags = {"bussiness api"})
@Controller
@RequestMapping("/bussiness")
public class BussinessController {

    public Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IBussinessService  bService;

    @ApiOperation("testGet接口")
    @GetMapping("/testGet")
    @ApiResponses({@ApiResponse(code = 200,response = SysConfig.class,message = "success")})
    @ResponseBody
    public Object getSysConfig(@RequestParam(name ="userId")String userId,
                               @RequestParam(name ="channel")Integer channel,
                               @RequestParam(name ="account")String account,
                               @RequestHeader(required = false) Map<String,String> header){

        //throw new ServiceException(NO_PERMITION);

        logger.info("recv userId = {},channel={},account={}  header = {}"
                ,userId,channel,account,JSON.toJSONString(header));

        return bService.getSysConfig();
    }



    @ApiOperation("testPost接口")
    @PostMapping("/testPost")
    @ResponseBody
    public Object getSysConfigByJson(@RequestBody GetSysConfReq req,@RequestHeader(name = "RL",required = false) String rl){

        //throw new ServiceException(NO_PERMITION);
        logger.info("recv req = {}  rl = {}",JSON.toJSONString(req),JSON.toJSONString(rl));

        return bService.getSysConfig();
    }



}
