package com.boylegu.springboot_vue.service;

import com.avos.avoscloud.AVOSCloud;
import org.springframework.stereotype.Component;

@Component
public class LeanCloud {

    // 参数依次为 AppId、AppKey、MasterKey
    AVOSCloud.initialize("Eul6rG90rOjIO5imP853JOmn-gzGzoHsz","XdmDTh1MQGHCYrJjp1B5Jyh1","8f51dePoE2N9xItvT0jp5jHB");

}
