package com.jacketus.RSOI_Lab2.gatewayservice.service;

import com.jacketus.RSOI_Lab2.gatewayservice.redisq.JedisManager;
import com.jacketus.RSOI_Lab2.gatewayservice.redisq.WorkThread;

import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class GatewayServiceImplementation implements GatewayService {
    final private String booksServiceUrl = "http://localhost:8070";
    final private String usersServiceUrl = "http://localhost:8071";
    final private String licensesServiceUrl = "http://localhost:8072";

    // @Value( "${client.id}" )
    private String client_id = "gateway";
    // @Value( "${client_secret}" )
    private String client_secret = "secret";

    private String gateway_token = "";
    private String books_token = "";
    private String licenses_token = "";
    private String users_token = "";

    private Jedis jedis = new Jedis("127.0.0.1", 6379);
    private JedisManager jedisManager = new JedisManager(jedis);
    private WorkThread workThread = new WorkThread(jedis);



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

    private HttpUriRequest buildRequestWithAuth(HttpUriRequest request, StringBuilder token) throws IOException {
        request.removeHeaders("Authorization");
        request.addHeader("Authorization", "Bearer " + token.toString());
        return request;
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
                JSONObject a = new JSONObject();
                try {
                    a.put("error", "Service " + service_url + " temporarily unavailable");
                } catch (JSONException ignored) {
                }
                response.setEntity(new StringEntity(a.toString()));
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
    public String getBooks(PageRequest p) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(booksServiceUrl + "/books?page="+p.getPageNumber() + "&size=" + p.getPageSize());
        HttpResponse response;

        StringBuilder sb = new StringBuilder(books_token);
        response = this.executeRequestWithAuth(request, sb, booksServiceUrl);
        books_token = sb.toString();

        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public String getBookByID(Long bookID) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(booksServiceUrl + "/books/"+bookID);
        HttpResponse response;

        StringBuilder sb = new StringBuilder(books_token);
        response = this.executeRequestWithAuth(request, sb, booksServiceUrl);
        books_token = sb.toString();

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

    // Деградация.
    @Override
    public ResponseEntity getBooksByUser(Long userId) throws IOException {
        String res = "", res2 = "";
        int status_code = 200;
        JSONObject json_res = new JSONObject();

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(usersServiceUrl + "/users/"+userId);
        HttpResponse response1;

        StringBuilder sb = new StringBuilder(users_token);
        response1 = this.executeRequestWithAuth(request, sb, usersServiceUrl);
        users_token = sb.toString();

        res = EntityUtils.toString(response1.getEntity());
        if (response1.getStatusLine().getStatusCode() != 200)
            status_code = response1.getStatusLine().getStatusCode();
        System.out.println(res);

        request = new HttpGet(licensesServiceUrl + "/licenses/find/?user_id=" + userId);
        HttpResponse response2;

        StringBuilder sb2 = new StringBuilder(licenses_token);
        response2 = this.executeRequestWithAuth(request, sb2, licensesServiceUrl);

        licenses_token = sb2.toString();
        res2 = EntityUtils.toString(response2.getEntity());
        System.out.println(res2);

        if (response2.getStatusLine().getStatusCode() != 200)
            status_code = response2.getStatusLine().getStatusCode();

        try {
            JSONObject json1 = new JSONObject(res);
            JSONArray json2;
            try {
                json2 = new JSONArray(res2);
            } catch (JSONException e) {
                json2 = new JSONArray();
                json2.put(new JSONObject(res2));
            }
            json1.put("books", json2);

            return ResponseEntity.status(status_code).body(json1.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            try {
                json_res.put("error", "JSON Parse error");
            } catch (JSONException ignore) {
            }
        }

        return ResponseEntity.status(status_code).body(json_res.toString());
    }



    @Override
    public String getUserBook(Long userId, Long bookId) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(licensesServiceUrl + "/licenses/check?user_id=" + userId + "&book_id=" + bookId);
        HttpResponse response;

        StringBuilder sb = new StringBuilder(licenses_token);
        response = this.executeRequestWithAuth(request, sb, licensesServiceUrl);
        licenses_token = sb.toString();

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
                res.put("license", this.getLicense(p_id));
                request = new HttpGet(booksServiceUrl + "/books/" + bookId);
                HttpResponse response3;

                StringBuilder sb2 = new StringBuilder(books_token);
                response3 = this.executeRequestWithAuth(request, sb2, booksServiceUrl);
                books_token = sb2.toString();

                if (response3.getStatusLine().getStatusCode() == 200)
                    res.put("book", EntityUtils.toString(response3.getEntity()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res.toString();
    }

    @Override
    public String getLicense(Long ID) throws IOException {
        String res = "";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(licensesServiceUrl + "/licenses/" + ID);
        HttpResponse response2;

        StringBuilder sb2 = new StringBuilder(licenses_token);
        response2 = this.executeRequestWithAuth(request, sb2, licensesServiceUrl);
        licenses_token = sb2.toString();

        res += EntityUtils.toString(response2.getEntity());

        return res;
    }


    // Очередь
    @Override
    public ResponseEntity licenseBook(@RequestBody String license) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        int status_code = 200;
        JSONObject res = new JSONObject();
        String p2 = license;
        StringEntity p = new StringEntity(license);

        // Проверяем, есть ли такие песни в базах
        Long bookID = -1L;
        Long userID = -1L;
        try {
            JSONObject j = new JSONObject(p2);
            bookID = j.getLong("bookID");
            userID = j.getLong("userID");
        } catch (Exception e) {

            // Даннные неверны.
            status_code = 400;
            try {
                res.put("error", "Bad request");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return ResponseEntity.status(status_code).body(res.toString());
        }

        // Проверка, есть ли такая песня
        HttpGet request1 = new HttpGet(booksServiceUrl + "/books/"+bookID);
        HttpResponse response1;

        StringBuilder sb1 = new StringBuilder(books_token);
        response1 = this.executeRequestWithAuth(request1, sb1, booksServiceUrl);
        books_token = sb1.toString();

        // Проверка, есть ли такой пользователь
        HttpGet request2 = new HttpGet(usersServiceUrl + "/users/"+userID);
        HttpResponse response2;

        StringBuilder sb2 = new StringBuilder(users_token);
        response2 = this.executeRequestWithAuth(request2, sb2, usersServiceUrl);
        users_token = sb2.toString();

        if (!EntityUtils.toString(response1.getEntity()).isEmpty() &&
                !EntityUtils.toString(response2.getEntity()).isEmpty()) {


            HttpPost request3 = new HttpPost(licensesServiceUrl + "/licenses");
            request3.addHeader("content-type", "application/json");
            request3.setEntity(p);
            HttpResponse response3;

            try {
                StringBuilder sb3 = new StringBuilder(licenses_token);
                response3 = this.executeRequestWithAuth(request3, sb3, licensesServiceUrl);
                licenses_token = sb3.toString();
                if (response3.getStatusLine().getStatusCode() != 200) {
                    throw new Exception("Licenses not available");
                }
            } catch (Exception e) {
                // Ошибка. Добавляем в редиску
                request3 = (HttpPost)buildRequestWithAuth(request3, new StringBuilder(licenses_token));
                try {
                    jedisManager.addReqToQueue("POST", request3, Base64.getEncoder().encodeToString((client_id + ":" + client_secret).getBytes()), licensesServiceUrl);
                    workThread.start();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                // Пока очередь делает своё дело, доделываем оставшееся.
            }

            // Обновление счетчика песен у пользователя
            HttpPost request4 = new HttpPost(usersServiceUrl + "/users/"+userID+"/buy");

            StringBuilder sb4 = new StringBuilder(users_token);
            this.executeRequestWithAuth(request4, sb4, usersServiceUrl);
            users_token = sb4.toString();

            // Обновление счетчика покупок у песни
            HttpPost request5 = new HttpPost(booksServiceUrl + "/books/"+bookID+"/buy");

            StringBuilder sb5 = new StringBuilder(books_token);
            this.executeRequestWithAuth(request5, sb5, booksServiceUrl);
            books_token = sb5.toString();
        }
        else {
            System.out.println("No user or book");
            try {
                res.put("error", "User or book not found");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }


        return ResponseEntity.status(status_code).body(res.toString());
    }

    @Override
    public ResponseEntity addUser(@RequestBody String user) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        int status_code = 200;
        JSONObject res = new JSONObject();
        // Валидация
        Long userID = -1L;
        String login, name, pswd;
        try {
            JSONObject j = new JSONObject(user);
            // userID = j.getLong("userID");
            login = j.getString("username");
            // pswd = j.getString("password");
            //name = j.getString("name");
/*
            if (login.contains(" ") || name.contains(" ") ||
                login.contains("\"") || name.contains("\"") ||
                        login.contains("\'") || name.contains("\'"))
                throw new Exception();
*/
            j.put("login", login);
            j.put("name", login);
            user = j.toString();
        } catch (Exception e) {

            // Даннные неверны.
            status_code = 400;
            try {
                res.put("error", "Bad request");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return ResponseEntity.status(status_code).body(res.toString());
        }

        StringEntity p = new StringEntity(user);
        HttpPost request = new HttpPost(usersServiceUrl + "/users");
        request.addHeader("content-type", "application/json");
        request.setEntity(p);
        HttpResponse response;

        StringBuilder sb = new StringBuilder(users_token);
        response = this.executeRequestWithAuth(request, sb, usersServiceUrl);
        users_token = sb.toString();

        try {
            res.put("error", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(status_code).body(res.toString());
    }


    // Полный откат
    // Изменяются licenses и books. Их и откатываем, если что...
    @Override
    public ResponseEntity addRatingForBook(Long userID, Long bookID, int rate) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        JSONObject res = new JSONObject();

        int status_code = 200;
        if (userID < 0 || bookID < 0 || rate < 0 || rate > 5) {
            // Даннные неверны.
            status_code = 400;
            try {
                res.put("error", "Bad request");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return ResponseEntity.status(status_code).body(res.toString());
        }

        HttpGet request = new HttpGet(licensesServiceUrl + "/licenses/check?user_id=" + userID + "&book_id=" + bookID);
        HttpResponse response;

        StringBuilder sb = new StringBuilder(licenses_token);
        response = this.executeRequestWithAuth(request, sb, licensesServiceUrl);
        licenses_token = sb.toString();

        Long p_id = 0L;
        try {
            JSONObject p = new JSONObject(EntityUtils.toString(response.getEntity()));
            p_id = p.getLong("id");
        } catch (JSONException e) {
            // Даннные неверны.
            status_code = 400;
            try {
                res.put("error", "Bad request");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return ResponseEntity.status(status_code).body(res.toString());
        }


        JSONObject license = null;
        JSONObject book = null;

        HttpGet backupLicenses = new HttpGet(licensesServiceUrl + "/licenses/" + p_id);

        StringBuilder sb2 = new StringBuilder(licenses_token);

        HttpResponse r = this.executeRequestWithAuth(backupLicenses, sb2, licensesServiceUrl);
        licenses_token = sb2.toString();

        if (r.getStatusLine().getStatusCode() != 200) {
            status_code = r.getStatusLine().getStatusCode();
            try {
                res.put("error", "License service unavailable");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        else {
            try {
                license = new JSONObject(EntityUtils.toString(r.getEntity()));
            } catch (JSONException ignored) {
            }
        }
        HttpGet backupBooks = new HttpGet(booksServiceUrl + "/books/" + p_id);

        StringBuilder sb3 = new StringBuilder(books_token);
        HttpResponse rs = this.executeRequestWithAuth(backupBooks, sb3, booksServiceUrl);
        books_token = sb3.toString();

        if (rs.getStatusLine().getStatusCode() != 200) {
            status_code = rs.getStatusLine().getStatusCode();
            try {
                res.put("error", "Books service unavailable");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        else {
            try {
                book = new JSONObject(EntityUtils.toString(rs.getEntity()));
            } catch (JSONException ignored) {
            }
        }

        HttpPost request2 = new HttpPost(licensesServiceUrl + "/licenses/"+p_id+"/rate?rating=" + rate);
        HttpPost request3 = new HttpPost(booksServiceUrl + "/books/"+bookID+"/rate?rating=" + rate);
        HttpResponse resp;

        try {
            //httpClient.execute(request2);
            sb2 = new StringBuilder(licenses_token);
            resp = this.executeRequestWithAuth(request2, sb2, licensesServiceUrl);
            licenses_token = sb2.toString();
            if (resp.getStatusLine().getStatusCode() != 200) {
                status_code = rs.getStatusLine().getStatusCode();
                throw new Exception();
            }

            //httpClient.execute(request3);
            sb3 = new StringBuilder(books_token);
            resp = this.executeRequestWithAuth(request3, sb3, booksServiceUrl);
            books_token = sb3.toString();

            if (resp.getStatusLine().getStatusCode() != 200) {
                status_code = rs.getStatusLine().getStatusCode();
                throw new Exception();
            } else {
                try {
                    res.put("rollback", false);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (Exception e) {
            // Откатываемся. Статус коды сохранены.

            if (license != null) {
                HttpPost rollbackP = new HttpPost(licensesServiceUrl + "/licenses");
                rollbackP.setEntity(new StringEntity(license.toString()));
                httpClient.execute(rollbackP);
            }
            if (book != null) {
                HttpPost rollbackS = new HttpPost(booksServiceUrl + "/books");
                rollbackS.setEntity(new StringEntity(book.toString()));
                httpClient.execute(rollbackS);
            }
            try {
                res.put("rollback", true);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        return ResponseEntity.status(status_code).body(res.toString());
    }

    @Override
    public HttpResponse addBook(String book) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        StringEntity p = new StringEntity(book);
        HttpPost request = new HttpPost(booksServiceUrl + "/books");
        request.addHeader("content-type", "application/json");
        request.setEntity(p);
        HttpResponse response;

        StringBuilder sb = new StringBuilder(books_token);
        response = this.executeRequestWithAuth(request, sb, booksServiceUrl);
        books_token = sb.toString();

        return response;
    }

    @Override
    public void uploadBook(@RequestParam(value="file") MultipartFile book, String id) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpResponse response;


        File tempFile = null;
        try {
            String extension = ".uploaded";
            tempFile = File.createTempFile(book.getOriginalFilename(), extension);
            book.transferTo(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        org.apache.http.HttpEntity data = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addBinaryBody("file", book.getBytes(), ContentType.DEFAULT_BINARY, id + ".fb2")
                .addTextBody("text", book.getOriginalFilename(), ContentType.DEFAULT_BINARY)
                .build();

        HttpPost request = new HttpPost(booksServiceUrl + "/upload");
        request.setEntity(data);
       // request.addHeader("content-type", "multipart/form-data");

        StringBuilder sb = new StringBuilder(books_token);
        response = this.executeRequestWithAuth(request, sb, booksServiceUrl);
        books_token = sb.toString();
    }

    @Override
    public HttpResponse askToken(String url, String clientCred) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request1 = new HttpPost(url);
        request1.addHeader("Authorization", "Basic " + clientCred);

        return httpClient.execute(request1);
    }

    @Override
    public HttpResponse register_user(String auth_url, String user) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request1 = new HttpPost(auth_url + "/register_user");
        request1.addHeader("content-type", "application/json");
        request1.setEntity(new StringEntity(user));

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
    public File downloadBook(Long id) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpGet request = new HttpGet(booksServiceUrl + "/download/" + id);

        HttpResponse response;
        StringBuilder sb = new StringBuilder(books_token);
        response = this.executeRequestWithAuth(request, sb, booksServiceUrl);
        users_token = sb.toString();

        HttpEntity ent = response.getEntity();
        File myFile = new File("tmpfile");
        if (ent != null) {
            try (FileOutputStream outstream = new FileOutputStream(myFile)) {
                ent.writeTo(outstream);
            }
        }
        return myFile;
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


        request = new HttpGet(booksServiceUrl + "/check_health");
        sb = new StringBuilder(books_token);
        response = this.executeRequestWithAuth(request, sb, booksServiceUrl);
        books_token = sb.toString();
       /* if (response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 401 && response.getStatusLine().getStatusCode() != 403)
            array.put("Books service temporarily unavailable");
        else*/
            array.put(EntityUtils.toString(response.getEntity()));


        request = new HttpGet(licensesServiceUrl + "/check_health");
        sb = new StringBuilder(licenses_token);
        response = this.executeRequestWithAuth(request, sb, licensesServiceUrl);
        licenses_token = sb.toString();
        /*if (response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 401 && response.getStatusLine().getStatusCode() != 403)
            array.put("Licenses service temporarily unavailable");
        else*/
            array.put(EntityUtils.toString(response.getEntity()));

        return array.toString();
    }

    @Override
    public String searchBooks(String body, PageRequest p) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(booksServiceUrl + "/search?page="+p.getPageNumber() + "&size=" + p.getPageSize());
        HttpResponse response;

        request.setEntity(new StringEntity(body.toString()));

        StringBuilder sb = new StringBuilder(books_token);
        response = this.executeRequestWithAuth(request, sb, booksServiceUrl);
        books_token = sb.toString();

        return EntityUtils.toString(response.getEntity());

    }

    @Override
    public String putUser(String body) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPut request = new HttpPut(usersServiceUrl + "/users/edit");
        HttpResponse response;

        request.addHeader("content-type", "application/json");
        request.setEntity(new StringEntity(body.toString()));

        StringBuilder sb = new StringBuilder(users_token);
        response = this.executeRequestWithAuth(request, sb, usersServiceUrl);
        users_token = sb.toString();

        return EntityUtils.toString(response.getEntity());
    }
}