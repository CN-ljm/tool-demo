package com;

import com.tool.zip.ZipUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(App.class, args);

        ZipUtil.zipFileDir("E:\\material\\zz.zip","E:\\material\\image100");
    }
}
