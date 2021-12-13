package com.example.security.utills;

import com.example.security.domain.CMRespDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class Script {

    public static void responseData(HttpServletResponse response, CMRespDto cmRespDto){

        PrintWriter out;

        ObjectMapper mapper = new ObjectMapper();

        String jsonData = null;
        try {
            jsonData = mapper.writeValueAsString(cmRespDto);
            log.info("응답데이터=>{}",jsonData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        response.setHeader("Content-Type", "application/json; charset=utf-8");

        try {
            out = response.getWriter();
            out.println(jsonData);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
