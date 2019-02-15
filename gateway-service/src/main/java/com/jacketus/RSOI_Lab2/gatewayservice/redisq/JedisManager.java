package com.jacketus.RSOI_Lab2.gatewayservice.redisq;


import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
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

    public void addReqToQueue(String reqType, HttpRequest req, String cred, String auth_url) throws InterruptedException {
        JSONObject object = new JSONObject();

        System.out.println("add req to queue: "+ req.toString());
        if (reqType != "GET" && reqType != "POST" && reqType != "PUT" && reqType != "DELETE") {
            return;
        }

        try {
            object.put("req_type", reqType);
            object.put("url", req.getRequestLine().getUri());
            object.put("token", req.getFirstHeader("Authorization"));

            object.put("cred", cred);
            object.put("auth_url", auth_url);

            if (reqType == "POST") {
                object.put("body", EntityUtils.toString(  ((HttpPost)req).getEntity() ));
            }
            if (reqType == "PUT") {
                object.put("body", EntityUtils.toString(  ((HttpPut)req).getEntity() ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        bq.lpush("work", object.toString());
    }
}
