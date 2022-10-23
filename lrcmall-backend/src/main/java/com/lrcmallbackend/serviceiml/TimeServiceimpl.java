package com.lrcmallbackend.serviceiml;

import com.lrcmallbackend.service.TimeService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Date;

@Service
@SessionScope
public class TimeServiceimpl implements TimeService {
    long startTime;
    long endTime;

    @Override
    public void startTimer(){
        Date timer = new Date();
        startTime = timer.getTime();
    }

    @Override
    public String endTimer(){
        Date timer = new Date();
        endTime = timer.getTime();
        //返回Date格式持续时间
        long duringTime = endTime-startTime;
        long day = duringTime/(1000*60*60*24);
        long hour = duringTime/(1000*60*60);
        long minute = duringTime/(1000*60);
        long second = duringTime/(1000);
        String retString = day+"天"+hour+"时"+minute+"分"+second+"秒";
        return retString;
    }

}
