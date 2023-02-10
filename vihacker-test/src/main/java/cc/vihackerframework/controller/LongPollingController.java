package cc.vihackerframework.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 长轮询接口
 * Created By Ranger on 2023/2/10.
 */
@RestController
public class LongPollingController {
    private Random random =new Random();
    
    private AtomicLong sequenceId = new AtomicLong();
    
    private AtomicLong count = new AtomicLong();

    @RequestMapping("/longpolling")
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("第" + (count.incrementAndGet()) + "次 longpolling");
        int sleepSecends = random.nextInt(100);
        //随机获取等待时间，来通过sleep模拟服务端是否准备好数据
    
        System.out.println("wait " + sleepSecends + " second");
    
        try {
            TimeUnit.SECONDS.sleep(sleepSecends);//sleep
        } catch (InterruptedException e) {
        
        }
        PrintWriter out = response.getWriter();
        long value = sequenceId.getAndIncrement();
        out.write(Long.toString(value));
    }
}
