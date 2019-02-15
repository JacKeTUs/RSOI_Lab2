package com.jacketus.RSOI_Lab2.gatewayservice.service;

import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.Base64;

@Service
public class GatewayServiceImplementation implements GatewayService {
    final private String songsServiceUrl = "http://localhost:8070";
    final private String usersServiceUrl = "http://localhost:8071";
    final private String purchasesServiceUrl = "http://localhost:8072";

    // @Value( "${client.id}" )
    private String client_id = "gateway";
    // @Value( "${client_secret}" )
    private String client_secret = "secret";

    private String gateway_token = "";
    private String songs_token = "";
    private String purchases_token = "";
    private String users_token = "";



    @Override
    public String oauth_getcode(String auth_url, String client_id, String redirect_uri, String response_type) throws IOException {
        return (auth_url + "/oauth/authorize?grant_type=authorization_code&client_id="+client_id+"&redirect_uri="+redirect_uri+"&response_type="+response_type);
    }

    @Override
    public String oauth_exchangecode(String auth_url, String code, String redirect_uri, String client_cred) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost((auth_url + "/oauth/token?grant_type=authorization_code&code="+code+"&redirect_uri="+redirect_uri));

        request.addHeader("Authorization", "Basic " + client_cred);
        HttpResponse response = httpClient.execute(request);

        return EntityUtils.toString(response.getEntity());
    }




    private HttpResponse executeRequestWithAuth(HttpUriRequest request, StringBuilder token, String service_url) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpResponseFactory factory = new DefaultHttpResponseFactory();
        HttpResponse response = factory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_BAD_REQUEST, null), null);

        int i = 0;
        while (i < 3) {
            request.removeHeaders("Authorization");
            request.addHeader("Authorization", "Bearer " + token.toString());
            try {
                response = httpClient.execute(request);
            } catch (HttpHostConnectException e) {
                response.setStatusCode(500);
                response.setEntity(new StringEntity(String.format("Service %s temporarily unavailable", service_url)));
                return response;
            }
            if (response.getStatusLine().getStatusCode() == 401 || response.getStatusLine().getStatusCode() == 403) {
                token.delete(0, token.length());
                String t = "";
                HttpResponse hr = askToken(service_url + "/oauth/token?grant_type=client_credentials", Base64.getEncoder().encodeToString((client_id + ":" + client_secret).getBytes()));
                try {
                    JSONObject p = new JSONObject(EntityUtils.toString(hr.getEntity()));
                    t = p.getString("access_token");
                } catch (JSONException e) {
                    t = "";
                }
                token.append(t);
            } else
                //return response;
                break;
            i++;
        }

        return response;
    }

    @Override
    public String getSongs(PageRequest p) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(songsServiceUrl + "/songs?page="+p.getPageNumber() + "&size=" + p.getPageSize());
        HttpResponse response;

        StringBuilder sb = new StringBuilder(songs_token);
        response = this.executeRequestWithAuth(request, sb, songsServiceUrl);
        songs_token = sb.toString();

        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public String getSongByID(Long songID) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(songsServiceUrl + "/songs/"+songID);
        HttpResponse response;

        StringBuilder sb = new StringBuilder(songs_token);
        response = this.executeRequestWithAuth(request, sb, songsServiceUrl);
        songs_token = sb.toString();

        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public String getUserById(Long userId) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(usersServiceUrl + "/users/" + userId);
        HttpResponse response;

        StringBuilder sb = new StringBuilder(users_token);
        response = this.executeRequestWithAuth(request, sb, usersServiceUrl);
        users_token = sb.toString();

        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public String getSongsByUser(Long userId) throws IOException {
        String res = "", res2 = "";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(usersServiceUrl + "/users/"+userId);
        HttpResponse response1;

        StringBuilder sb = new StringBuilder(users_token);
        response1 = this.executeRequestWithAuth(request, sb, usersServiceUrl);
        users_token = sb.toString();

        res += EntityUtils.toString(response1.getEntity());
        request = new HttpGet(purchasesServiceUrl + "/purchases/find/?user_id=" + userId);
        HttpResponse response2;

        StringBuilder sb2 = new StringBuilder(purchases_token);
        response2 = this.executeRequestWithAuth(request, sb2, purchasesServiceUrl);
        purchases_token = sb2.toString();

        res2 += EntityUtils.toString(response2.getEntity());
        try {
            JSONObject json1 = new JSONObject(res);
            JSONArray json2 = new JSONArray(res2);
            json1.put("songs", json2);

            return json1.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public String getUserSong(Long userId, Long songId) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(purchasesServiceUrl + "/purchases/check?user_id=" + userId + "&song_id=" + songId);
        HttpResponse response;

        StringBuilder sb = new StringBuilder(purchases_token);
        response = this.executeRequestWithAuth(request, sb, purchasesServiceUrl);
        purchases_token = sb.toString();

        Long p_id = -1L;
        try {
            JSONObject p = new JSONObject(EntityUtils.toString(response.getEntity()));
            p_id = p.getLong("id");
        } catch (JSONException ignored) {
        }

        JSONObject res = new JSONObject();
        try {
            if (p_id == -1L) {
                res.put("license", "false");
            } else {
                res.put("license", "true");
                res.put("purchase", this.getPurchase(p_id));
                request = new HttpGet(songsServiceUrl + "/songs/" + songId);
                HttpResponse response3;

                StringBuilder sb2 = new StringBuilder(songs_token);
                response3 = this.executeRequestWithAuth(request, sb2, songsServiceUrl);
                songs_token = sb2.toString();

                res.put("song", EntityUtils.toString(response3.getEntity()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res.toString();
    }

    @Override
    public String getPurchase(Long ID) throws IOException {
        String res = "";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(purchasesServiceUrl + "/purchases/" + ID);
        HttpResponse response2;

        StringBuilder sb2 = new StringBuilder(purchases_token);
        response2 = this.executeRequestWithAuth(request, sb2, purchasesServiceUrl);
        purchases_token = sb2.toString();

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
        HttpResponse response1;

        StringBuilder sb1 = new StringBuilder(songs_token);
        response1 = this.executeRequestWithAuth(request1, sb1, songsServiceUrl);
        songs_token = sb1.toString();


        // Проверка, есть ли такой пользователь
        HttpGet request2 = new HttpGet(usersServiceUrl + "/users/"+userID);
        HttpResponse response2;

        StringBuilder sb2 = new StringBuilder(users_token);
        response2 = this.executeRequestWithAuth(request2, sb2, usersServiceUrl);
        users_token = sb2.toString();

        if (!EntityUtils.toString(response1.getEntity()).isEmpty() &&
                !EntityUtils.toString(response2.getEntity()).isEmpty()) {
            HttpPost request3 = new HttpPost(purchasesServiceUrl + "/purchases");
            request3.addHeader("content-type", "application/json");
            request3.setEntity(p);
            HttpResponse response3;

            StringBuilder sb3 = new StringBuilder(purchases_token);
            response3 = this.executeRequestWithAuth(request3, sb3, purchasesServiceUrl);
            purchases_token = sb3.toString();

            HttpPost request4 = new HttpPost(usersServiceUrl + "/users/"+userID+"/buy");
            //httpClient.execute(request4);

            StringBuilder sb4 = new StringBuilder(users_token);
            this.executeRequestWithAuth(request4, sb4, usersServiceUrl);
            users_token = sb4.toString();

            HttpPost request5 = new HttpPost(songsServiceUrl + "/songs/"+songID+"/buy");
            //httpClient.execute(request5);

            StringBuilder sb5 = new StringBuilder(songs_token);
            this.executeRequestWithAuth(request5, sb5, songsServiceUrl);
            songs_token = sb5.toString();
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
        HttpResponse response;

        StringBuilder sb = new StringBuilder(users_token);
        response = this.executeRequestWithAuth(request, sb, usersServiceUrl);
        users_token = sb.toString();
    }

    @Override
    public void addRatingForSong(Long userID, Long songID, int rate) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String res = "";

        HttpGet request = new HttpGet(purchasesServiceUrl + "/purchases/check?user_id=" + userID + "&song_id=" + songID);
        HttpResponse response;

        StringBuilder sb = new StringBuilder(purchases_token);
        response = this.executeRequestWithAuth(request, sb, purchasesServiceUrl);
        purchases_token = sb.toString();


        Long p_id = 0L;
        try {
            JSONObject p = new JSONObject(EntityUtils.toString(response.getEntity()));
            p_id = p.getLong("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpPost request2 = new HttpPost(purchasesServiceUrl + "/purchases/"+p_id+"/rate?rating=" + rate);

        HttpPost request3 = new HttpPost(songsServiceUrl + "/songs/"+songID+"/rate?rating=" + rate);

        httpClient.execute(request2);
        StringBuilder sb2 = new StringBuilder(purchases_token);
        this.executeRequestWithAuth(request2, sb2, purchasesServiceUrl);
        purchases_token = sb2.toString();

        httpClient.execute(request3);
        StringBuilder sb3 = new StringBuilder(songs_token);
        this.executeRequestWithAuth(request3, sb3, songsServiceUrl);
        songs_token = sb3.toString();
    }

    @Override
    public void addSong(@RequestBody String song) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        StringEntity p = new StringEntity(song);
        HttpPost request = new HttpPost(songsServiceUrl + "/songs");
        request.addHeader("content-type", "application/json");
        request.setEntity(p);
        HttpResponse response;

        StringBuilder sb = new StringBuilder(songs_token);
        response = this.executeRequestWithAuth(request, sb, songsServiceUrl);
        songs_token = sb.toString();
    }


    @Override
    public HttpResponse askToken(String url, String clientCred) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request1 = new HttpPost(url);
        request1.addHeader("Authorization", "Basic " + clientCred);

        return httpClient.execute(request1);
    }

    @Override
    public HttpResponse checkToken(String auth_url, String token) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request;
        HttpResponse response = null;
        int i = 0;
        while (i <= 3) {
            request = new HttpGet(auth_url + "/oauth/check_token?token=" + token);
            // request.addHeader("Authorization", "Bearer " + gateway_token);
            request.addHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((client_id + ":" + client_secret).getBytes()));
            response = httpClient.execute(request);

            if (response.getStatusLine().getStatusCode() == 401 || response.getStatusLine().getStatusCode() == 403) {
                HttpResponse r = askToken(auth_url + "/oauth/token?grant_type=client_credentials", Base64.getEncoder().encodeToString((client_id + ":" + client_secret).getBytes()));
                String t = "";
                try {
                    JSONObject p = new JSONObject(EntityUtils.toString(r.getEntity()));
                    t = p.getString("access_token");
                } catch (JSONException e) {
                    t = "";
                }
                this.gateway_token = t;
            } else
                break;
            i++;
        }
        return response;
    }

    @Override
    public String getUserByLogin(String username) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpGet request = new HttpGet(usersServiceUrl + "/users/find?username=" + username);

        HttpResponse response;
        StringBuilder sb = new StringBuilder(users_token);
        response = this.executeRequestWithAuth(request, sb, usersServiceUrl);
        users_token = sb.toString();

        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public String checkAllServices() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        JSONArray array = new JSONArray();

        HttpGet request = new HttpGet(usersServiceUrl + "/check_health");
        HttpResponse response;
        StringBuilder sb = new StringBuilder(users_token);
        response = this.executeRequestWithAuth(request, sb, usersServiceUrl);
        users_token = sb.toString();
        /*
        if (response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 401 && response.getStatusLine().getStatusCode() != 403)
            array.put("Users service temporarily unavailable");
        else*/
            array.put(EntityUtils.toString(response.getEntity()));


        request = new HttpGet(songsServiceUrl + "/check_health");
        sb = new StringBuilder(songs_token);
        response = this.executeRequestWithAuth(request, sb, songsServiceUrl);
        songs_token = sb.toString();
       /* if (response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 401 && response.getStatusLine().getStatusCode() != 403)
            array.put("Songs service temporarily unavailable");
        else*/
            array.put(EntityUtils.toString(response.getEntity()));


        request = new HttpGet(purchasesServiceUrl + "/check_health");
        sb = new StringBuilder(purchases_token);
        response = this.executeRequestWithAuth(request, sb, purchasesServiceUrl);
        purchases_token = sb.toString();
        /*if (response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 401 && response.getStatusLine().getStatusCode() != 403)
            array.put("Purchases service temporarily unavailable");
        else*/
            array.put(EntityUtils.toString(response.getEntity()));

        return array.toString();
    }
}