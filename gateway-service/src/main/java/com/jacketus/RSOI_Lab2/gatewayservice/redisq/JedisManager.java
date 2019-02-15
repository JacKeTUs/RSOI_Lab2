package com.jacketus.RSOI_Lab2.gatewayservice.redisq;


import org.json.JSONException;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

public class JedisManager {
    private Jedis bq = null;

    public void setBlockingQueue(Jedis bq) {
        this.bq = bq;
    }

    public JedisManager(Jedis j) {
        this.setBlockingQueue(j);
    }

    public void addUrlToQueue(String reqType, String url, String body) throws InterruptedException {
        JSONObject object = new JSONObject();

        if (reqType != "GET" && reqType != "POST" && reqType != "PUT" && reqType != "DELETE") {
            return;
        }

        try {
            object.put("req_type", reqType);
            object.put("url", url);
            object.put("body", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        bq.lpush("work", object.toString());
    }
}
