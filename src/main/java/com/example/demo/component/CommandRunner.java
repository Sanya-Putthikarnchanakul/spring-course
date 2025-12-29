package com.example.demo.component;

import com.example.demo.service.client.EchoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.MessageFormat;

@Component
public class CommandRunner {

    private static final Logger log = LoggerFactory.getLogger(CommandRunner.class);

    @Value("${file.path}")
    private String filepath;

    @Bean
    CommandLineRunner runner() {
        return args -> {
            // Command + Option + T to surround code with foldable
            //region Try echo
            /*
            log.info("Start");

            var requestBody = new HashMap<String, Object>();
            requestBody.put("fullname", "Sanya P.");
            requestBody.put("age", 37);

            Map<?, ?> responseBody = echoClient.echo(requestBody);

            Map<?, ?> body = (Map<?, ?>)responseBody.get("body");
            String fullname = (String)body.get("fullname");
            int age = (int)body.get("age");
            log.info("Response Body={},{},{}", body, fullname, age);

            log.info("End");
            */
            //endregion

            // region

            File file;
            try {
                log.debug("filepath={}", filepath);
                file = new File(filepath);

                boolean isNewFileCreated = file.createNewFile();
                if (!isNewFileCreated) {
                    log.warn("File exist");
                    return;
                }

                log.debug("New file {} created", file.getName());
            } catch (Exception e) {
                log.error("Error", e);
            }

            // endregion
        };
    }

}
