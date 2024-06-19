package com.bda.dcp.flume.interceptor;

import com.google.common.base.Splitter;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by fengxuguang on 2024/6/14 15:32
 */
@Slf4j
public class JsonInterceptor implements Interceptor {

    private String jsonTopic;

    public JsonInterceptor(String jsonTopic) {
        this.jsonTopic = jsonTopic;
        System.out.println("======> keyword = " + jsonTopic);
    }

    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {
        try {
            String body = new String(event.getBody());
            System.out.println("=====> body = " + body);
            log.info("=====> body: {}", body);
            System.out.println("======> keyword = " + jsonTopic);

            Gson gson = new Gson();
            Map<String, String> bodyJson = gson.fromJson(body, Map.class);
            log.info("=====> body json: {}", bodyJson);

            List<String> columns = new ArrayList<>();
            if (StringUtils.isNotBlank(jsonTopic)) {
                Iterable<String> split = Splitter.on(",")
                        .omitEmptyStrings()
                        .trimResults()
                        .split(jsonTopic);
                split.forEach(s -> columns.add(s));
            }
            Map<String, String> result = new HashMap<>();
            for (String key : bodyJson.keySet()) {
                if (columns.contains(key)) {
                    result.put(key, bodyJson.get(key));
                }
            }

            event.setBody(gson.toJson(result).getBytes());
            return event;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<Event> intercept(List<Event> list) {
        List<Event> resultList = Lists.newArrayList();
        for (Event event : list) {
            Event result = intercept(event);
            if (result != null) {
                resultList.add(result);
            }
        }
        return resultList;
    }

    @Override
    public void close() {

    }

    public static class Builder implements Interceptor.Builder {

        private String jsonTopic;

        @Override
        public Interceptor build() {
            return new JsonInterceptor(jsonTopic);
        }

        @Override
        public void configure(Context context) {
            jsonTopic = context.getString("jsonTopic");
        }
    }

}
