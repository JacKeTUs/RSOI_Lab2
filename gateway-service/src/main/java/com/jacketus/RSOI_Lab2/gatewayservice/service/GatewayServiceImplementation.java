package com.jacketus.RSOI_Lab2.gatewayservice.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

@Service
public class GatewayServiceImplementation implements GatewayService {
    final private String songsServiceUrl = "http://localhost:8070";
    final private String usersServiceUrl = "http://localhost:8071";
    final private String purchasesServiceUrl = "http://localhost:8072";

    @Override
    public String getSongs() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(songsServiceUrl + "/songs");
        HttpResponse response = httpClient.execute(request);

        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public String getSongByID(Long songID) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(songsServiceUrl + "/songs/"+songID);
        HttpResponse response = httpClient.execute(request);

        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public String getUserById(Long userId) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(usersServiceUrl + "/users/" + userId);
        HttpResponse response = httpClient.execute(request);

        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public String getSongsByUser(Long userId) throws IOException {
        String res = "";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(usersServiceUrl + "/users/"+userId);
        HttpResponse response1 = httpClient.execute(request);

        res += EntityUtils.toString(response1.getEntity());
        request = new HttpGet(purchasesServiceUrl + "/purchases/find/?user_id=" + userId);
        HttpResponse response2 = httpClient.execute(request);

        res += "\n" + EntityUtils.toString(response2.getEntity());

        return res;
    }

    @Override
    public String getUserSong(Long userId, Long songId) throws IOException {
        String res = "";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(purchasesServiceUrl + "/purchases/check?user_id=" + userId + "&song_id=" + songId);
        HttpResponse response = httpClient.execute(request);
        Long p_id = 0L;
        try {
            JSONObject p = new JSONObject(EntityUtils.toString(response.getEntity()));
            p_id = p.getLong("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        res += this.getPurchase(p_id);

        request = new HttpGet(songsServiceUrl + "/songs/" + songId);
        HttpResponse response3 = httpClient.execute(request);
        res += EntityUtils.toString(response3.getEntity());

        return res;
    }

    @Override
    public String getPurchase(Long ID) throws IOException {
        String res = "";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(purchasesServiceUrl + "/purchases/" + ID);
        HttpResponse response2 = httpClient.execute(request);
        res += EntityUtils.toString(response2.getEntity());

        return res;
    }

    @Override
    public void purchaseSong(@RequestBody String purchase) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String p2 = purchase;
        StringEntity p = new StringEntity(purchase);

        // Проверяем, есть ли такие песни в базах
        Long songID = -1L;
        Long userID = -1L;
        try {
            JSONObject j = new JSONObject(p2);
            songID = j.getLong("songID");
            userID = j.getLong("userID");
        } catch (Exception e) {
            throw new IOException();
        }

        // Проверка, есть ли такая песня
        HttpGet request1 = new HttpGet(songsServiceUrl + "/songs/"+songID);
        HttpResponse response1 = httpClient.execute(request1);

        // Проверка, есть ли такой пользователь
        HttpGet request2 = new HttpGet(usersServiceUrl + "/users/"+userID);
        HttpResponse response2 = httpClient.execute(request2);

        if (!EntityUtils.toString(response1.getEntity()).isEmpty() &&
                !EntityUtils.toString(response2.getEntity()).isEmpty()) {
            HttpPost request3 = new HttpPost(purchasesServiceUrl + "/purchases");
            request3.addHeader("content-type", "application/json");
            request3.setEntity(p);
            HttpResponse response3 = httpClient.execute(request3);

            HttpPost request4 = new HttpPost(usersServiceUrl + "/users/"+userID+"/buy");
            httpClient.execute(request4);

            HttpPost request5 = new HttpPost(songsServiceUrl + "/songs/"+songID+"/buy");
            httpClient.execute(request5);
        }
        else
            System.out.println("No user or song");
    }

    @Override
    public void addUser(@RequestBody String user) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        StringEntity p = new StringEntity(user);
        HttpPost request = new HttpPost(usersServiceUrl + "/users");
        request.addHeader("content-type", "application/json");
        request.setEntity(p);
        HttpResponse response = httpClient.execute(request);
    }

    @Override
    public void addRatingForSong(Long userID, Long songID, int rate) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String res = "";

        HttpGet request = new HttpGet(purchasesServiceUrl + "/purchases/check?user_id=" + userID + "&song_id=" + songID);
        HttpResponse response = httpClient.execute(request);
        Long p_id = 0L;
        try {
            JSONObject p = new JSONObject(EntityUtils.toString(response.getEntity()));
            p_id = p.getLong("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpPost request2 = new HttpPost(purchasesServiceUrl + "/purchases/"+p_id+"/rate&rating=" + rate);

        HttpPost request3 = new HttpPost(songsServiceUrl + "/songs/"+songID+"/rate&rating=" + rate);

        httpClient.execute(request2);
        httpClient.execute(request3);
    }


    @Override
    public void addSong(@RequestBody String song) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        StringEntity p = new StringEntity(song);
        HttpPost request = new HttpPost(songsServiceUrl + "/songs");
        request.addHeader("content-type", "application/json");
        request.setEntity(p);
        HttpResponse response = httpClient.execute(request);
    }
}
