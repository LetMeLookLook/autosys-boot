package com.autosys.system;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@SpringBootApplication
public class AutosysSystemApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(AutosysSystemApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String contextpath = env.getProperty("server.servlet.context-path");
        log.info("\n----------------------------------------------------------\n\t" +
                "Application Autosys-Boot is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + contextpath + "/\n\t" +
                "External: \thttp://" + ip + ":" + port + contextpath +"/\n\t" +
                "Doc: \t\thttp://" + ip + ":" + port + contextpath + "/doc.html\n" +
                "----------------------------------------------------------");
    }

}
