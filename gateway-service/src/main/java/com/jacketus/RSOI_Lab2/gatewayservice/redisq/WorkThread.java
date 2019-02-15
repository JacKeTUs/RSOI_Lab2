package com.jacketus.RSOI_Lab2.gatewayservice.redisq;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.io.IOException;

public class WorkThread implements Runnable {

    protected Jedis queue = null;
    final private int timeout_sec = 2;
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

            CloseableHttpResponse httpResponse = null;
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
                requestBase.addHeader("content-type", "application/json");
                requestBase.addHeader("Authorization", "Bearer " + req.getString("token"));

                httpResponse = httpClient.execute(requestBase);
                if (httpResponse.getStatusLine().getStatusCode() == 401 || httpResponse.getStatusLine().getStatusCode() == 403) {
                    // Попробовать авторизоваться
                    String t = this.askToken(req.getString("auth_url"), req.getString("cred"));
                    System.out.println("new token: " + t);
                    requestBase.removeHeaders("Authorization");
                    requestBase.addHeader("Authorization", "Bearer " + t);
                    req.remove("token");
                    req.put("token", t);

                    queue.lpush("work", req.toString());
                    try {
                        Thread.sleep(timeout_sec * 1000);
                    } catch (InterruptedException e1) { }
                }

            } catch (Exception e) {
                //  Если что-то опять пошло не так, добавляем запрос снова в очередь
                System.out.println("Service unavailable, added to queue again");
                queue.lpush("work", req.toString());
                try {
                    Thread.sleep(timeout_sec * 1000);
                } catch (InterruptedException e1) { }
            }

        }
        this.thread.stop();
    }

    private String askToken(String url, String cred) {
        String t = "";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request1 = new HttpPost(url + "/oauth/token?grant_type=client_credentials");
        request1.addHeader("Authorization", "Basic " + cred);
        try {
            HttpResponse hr = httpClient.execute(request1);
            JSONObject p = new JSONObject(EntityUtils.toString(hr.getEntity()));
            t = p.getString("access_token");
        } catch (Exception e) {
            t = "";
        }
        return t;
    }
}
