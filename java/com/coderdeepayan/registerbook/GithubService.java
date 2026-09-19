package com.coderdeepayan.registerbook;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GithubService {
    private String token = "github_pat_11AZNNDGA03TjljswQIjBa_njsLhPW7jZdx" +
            "9d7knyGAjZPjs5tZvnPmLbLtv0w7yi3T3FN6UZ2PBp9OdMH",
            owner = "Deepayan2005",repo = "RegisterBook",path = "data.txt";
    private Set<Creditor> creditorIdSet = new HashSet<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int uploadRecords(JSONObject content) throws Exception {

        URL url = new URL("https://api.github.com/repos/" + owner + "/" + repo + "/contents/" + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Accept", "application/vnd.github+json");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        StringBuilder response = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null){
            response.append(line);
        }
        br.close();
        String sha = new JSONObject(response.toString()).getString("sha");
        String encoded = Base64.getEncoder().encodeToString(content.toString().getBytes(StandardCharsets.UTF_8));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message","Updating data.txt");
        jsonObject.put("content",encoded);
        jsonObject.put("sha",sha);

        URL url2 = new URL("https://api.github.com/repos/" + owner + "/" + repo + "/contents/" + path);

        HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
        conn2.setRequestMethod("PUT");
        conn2.setDoOutput(true);
        conn2.setRequestProperty("Authorization", "Bearer " + token);
        conn2.setRequestProperty("Accept", "application/vnd.github+json");
        conn2.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = conn2.getOutputStream()) {
            os.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
        }

        return conn2.getResponseCode();
    }
    public List<DataRecord> getRecords() throws Exception {
        List<DataRecord> dataRecordList= null;
        URL url = new URL("https://raw.githubusercontent.com/Deepayan2005/RegisterBook/main/data.txt");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null){
            response.append(line);
        }

        br.close();
        if (conn.getResponseCode()==200){
            dataRecordList = new ArrayList<>();
        }
        JSONArray jsonArray = new JSONObject(response.toString()).getJSONArray("data");
        for (int i = 0; i <jsonArray.length() ; i++) {
            String name = jsonArray.getJSONObject(i).getString("name");
            String id = jsonArray.getJSONObject(i).getString("id");
            creditorIdSet.add(new Creditor(name,id,false));
            JSONArray jsonArray2 = jsonArray.getJSONObject(i).getJSONArray("bill");
            for (int j = 0; j <jsonArray2.length() ; j++) {
                dataRecordList.add(
                        new DataRecord(name, id,jsonArray2.getJSONObject(j).getString("purpose"),
                                jsonArray2.getJSONObject(j).getString("date"),
                                jsonArray2.getJSONObject(j).getDouble("cost"),
                                DataRecord.RECORD));
            }
        }
        return dataRecordList;
    }

    public Set<Creditor> getCreditorIdSet() {
        return creditorIdSet;
    }
}
