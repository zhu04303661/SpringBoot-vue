package com.boylegu.springboot_vue;

import com.avos.avoscloud.AVOSCloud;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;


@Configuration
@SpringBootApplication
public class App {

	public static void main(String[] args) {

		AVOSCloud.initialize("Eul6rG90rOjIO5imP853JOmn-gzGzoHsz","XdmDTh1MQGHCYrJjp1B5Jyh1","8f51dePoE2N9xItvT0jp5jHB");
		AVOSCloud.setDebugLogEnabled(true);

		SpringApplication.run(App.class, args);

	}
}
