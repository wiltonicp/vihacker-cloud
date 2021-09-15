package cc.vihackerframework.es.controller;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.api.ViHackerResult;
import cc.vihackerframework.core.encrypt.annotation.Encrypt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ranger on 2021/9/15
 */
@RestController
public class DemoController {

    @Encrypt
    @GetMapping("/")
    public ViHackerApiResult test(){
        Map<String,Object> map = new HashMap<>();
        map.put("username",1881818181);
        map.put("password","admin");
        return ViHackerApiResult.data(map);
    }
}
