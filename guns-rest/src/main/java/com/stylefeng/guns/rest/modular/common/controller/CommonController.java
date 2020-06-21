package com.stylefeng.guns.rest.modular.common.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.intercepter.ApiIdempotent;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.core.util.*;
import com.stylefeng.guns.core.util.redis.RedisService;
import com.stylefeng.guns.persistence.dao.CommonDataMapper;
import com.stylefeng.guns.persistence.model.CommonData;
import com.stylefeng.guns.rest.constants.Biz;
import com.stylefeng.guns.rest.core.util.BaseJson;
import com.stylefeng.guns.rest.modular.common.service.ICommonService;
import com.stylefeng.guns.rest.modular.user.service.IUserService;
import com.stylefent.guns.entity.query.CommonQuery;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author: lx
 */
@RestController
public class CommonController {

    @Autowired
    private OSSUtil ossUtil;



    @Autowired
    private CommonDataMapper commonDataMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ICommonService commonService;

    @Autowired
    private JiGuangPushUtil jiGuangPushUtil;

    /**
     * @description: 图片上传
     * @author: lx
     */
    @PostMapping("/common/upload")
    public BaseJson ueUpload(@RequestParam(value = "file") MultipartFile file,
                             @RequestParam(value = "type", required = false, defaultValue = "default") String fileType) {
        Map<String, Object> resultMap = new HashMap<>();
        String filePath = ossUtil.uploadObject2OSS(file, fileType);
        resultMap.put("imagePath", (ossUtil.getENV() ? ossUtil.getREQUEST_URL() : ossUtil.getREQUEST_URL_TEST()) + filePath);
//        resultMap.put("savePath", filePath);
//        resultMap.put("fileName", file.getOriginalFilename().split("\\.")[0]);
        return BaseJson.ofSuccess(resultMap);
    }

    /**
     * @description: 图片上传
     * @author: lx
     */
    @PostMapping("/common/multiUpload")
    public BaseJson multiUpload(@RequestParam(value = "file") MultipartFile[] file,
                                @RequestParam(value = "type", required = false, defaultValue = "default") String fileType) {
        List<Map> files = new ArrayList<>();
        Map<String, Object> resultMap;
        if (file == null || file.length <= 0) {
            return BaseJson.ofFail("请上传图片");
        }
        for (MultipartFile multipartFile : file) {
            resultMap = new HashMap<>();
            String filePath = ossUtil.uploadObject2OSS(multipartFile, fileType);
            resultMap.put("imagePath", (ossUtil.getENV() ? ossUtil.getREQUEST_URL() : ossUtil.getREQUEST_URL_TEST()) + filePath);
//            resultMap.put("savePath", filePath);
//            resultMap.put("fileName", multipartFile.getOriginalFilename().split("\\.")[0]);
            files.add(resultMap);
        }
        return BaseJson.ofSuccess(files);
    }


    /**
     * @description: 公共数据
     * @author: lx
     */
    @PostMapping("/common/commonData")
    public BaseJson commonData(CommonQuery query){
        String keyword = query.getIds();
        if(StrKit.isEmpty(keyword)){
            BaseJson.ofFail(Biz.INVALID_PARAM);
        }
        String[] split = keyword.split(",");
        List<CommonData> commonDatas = commonDataMapper.selectList(new EntityWrapper<CommonData>().in("id", split));
        return BaseJson.ofSuccess(commonDatas);
    }


    @PostMapping("/common/checkVersion")
    public BaseJson checkVersion(CommonQuery query) {
        String ios_max = commonDataMapper.selectById(24).getValue();//ios_show
        String and_max = commonDataMapper.selectById(25).getValue();//and_show
        String and_min = commonDataMapper.selectById(26).getValue();//and_update
        String ios_min = commonDataMapper.selectById(27).getValue();//ios_update
        String download = commonDataMapper.selectById(28).getExtra();//ios_update
        Map map = new HashMap();
        map.put("ios_max",ios_max);
        map.put("ios_min",ios_min);
        map.put("and_max",and_max);
        map.put("and_min",and_min);
        map.put("updateDetail",download);
        return BaseJson.ofSuccess(map);
    }


    @RequestMapping("/getMethodToken")
    public BaseJson getMethodToken(){
        String token = ApiTokenUtil.createToken();
        return BaseJson.ofSuccess(MapUtil.build().put("token",token ).over());
    }

    @RequestMapping("/tokenMethod")
    @ApiIdempotent
    public BaseJson tokenMethod(){
        return BaseJson.ofSuccess(MapUtil.build().put("flag",true ).over());
    }

    @Autowired
    RabbitTemplate rabbitTemplate;  //使用RabbitTemplate,这提供了接收/发送等等方法

    @GetMapping("/sendDirectMessage")
    public String sendDirectMessage() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map=new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageData",messageData);
        map.put("createTime",createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", map);
        return "ok";
    }

}
