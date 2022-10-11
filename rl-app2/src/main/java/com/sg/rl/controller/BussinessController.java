package com.sg.rl.controller;


import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = {"app2 api"})
@Controller
@RequestMapping("/bussiness")
public class BussinessController {

    public Logger logger = LoggerFactory.getLogger(getClass());


    @ApiOperation("testGet接口")
    @GetMapping("/testGet")
    @ApiResponses({@ApiResponse(code = 200,message = "success")})
    @ResponseBody
    public Object getSysConfig(@RequestParam(name ="userId")String userId,
                               @RequestParam(name ="channel")Integer channel,
                               @RequestParam(name ="account")String account,
                               @RequestHeader(required = false) Map<String,String> header){

        //throw new ServiceException(NO_PERMITION);

        logger.info("recv userId = {},channel={},account={}  header = {}"
                ,userId,channel,account,JSON.toJSONString(header));

        return null;
    }


}
