package cc.vihackerframework.string.controller;

import cc.vihackerframework.string.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Ranger on 2022/5/16.
 */
@Controller
public class StringController {

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @PostMapping("/getString")
    public@ResponseBody String getString(String text){
        String[] split = text.split("\n");
        List<String> list = Arrays.asList(split);
        Set<String> set = new HashSet<>();
        for (int i = 0; i < list.size(); i++) {
            for (int j = list.size() - 1; j > i; j--) {
                String maxSameString = StringUtils.getMaxSameString(list.get(i).substring(0,15), list.get(j).substring(0,15));
                if(maxSameString!= null && maxSameString != "" && maxSameString.length() > 5 && maxSameString.length() <= 15 && StringUtils.check(maxSameString)){
                    set.add("<pre>" + maxSameString + "</pre>");
                }
            }
        }
        String join = String.join("\r\n", set);
        return join;
    }
}
