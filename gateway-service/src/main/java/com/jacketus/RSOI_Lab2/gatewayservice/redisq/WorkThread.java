package com.jacketus.RSOI_Lab2.gatewayservice.redisq;

import jdk.nashorn.internal.parser.JSONParser;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.io.IOException;

public class WorkThread implements Runnable {

    protected Jedis queue = null;
    Thread thread;

    public WorkThread(Jedis queue) {
        this.queue = queue;
        this.thread = new Thread(this);
    }
    public void start() {
        this.thread.start();
    }

    public void run() {
        while(queue.exists("work")) {
            System.out.println("Here we go again");

            JSONObject req = null;
            try {
                req = new JSONObject(queue.rpop("work"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {

                CloseableHttpClient httpClient = HttpClients.createDefault();

                HttpRequestBase requestBase = null;
                String url = req.getString("url");
                switch (req.getString("req_type")) {
                    case ("GET"):
                        requestBase = new HttpGet(url);
                        break;
                    case ("POST"):
                        requestBase = new HttpPost(url);
                        ((HttpPost)requestBase).setEntity(new StringEntity(req.getString("body")));
                        break;
                    case ("PUT"):
                        requestBase = new HttpPut(url);
                        ((HttpPut)requestBase).setEntity(new StringEntity(req.getString("body")));
                        break;
                    case ("DELETE"):
                        requestBase = new HttpDelete(url);
                        break;
                }

                try {
                    CloseableHttpResponse httpResponse = httpClient.execute(requestBase);
                } catch (RuntimeException e){
                    //  Если что-то опять пошло не так, добавляем запрос снова в очередь
                    queue.lpush("work", req.toString());
                }

            } catch (Exception e) {
                //  Если что-то опять пошло не так, добавляем запрос снова в очередь
                queue.lpush("work", req.toString());
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e1) {
                }
            }
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                thread.stop();
            }
        }
    }
}
