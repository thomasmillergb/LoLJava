package api;

import api.JSONobjects.Summoner;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Thomas on 31/05/2015.
 */

public class APISummoner {
    private Summoner summoner;
    private String name;
    public APISummoner(String name){
        this.name = name;

    }
    public Summoner getSummoner(){
        return summoner;
    }
    public static Summoner getIDapi(String name) {
        String prefex = "https://euw.api.pvp.net";
        String sufex = "/api/lol/euw/v1.4/summoner/by-name/" + name;
        String key = "2d33b014-b236-4d80-88e4-60567ae5026c";
        String uri = prefex + sufex + "?api_key=" + key;
        System.out.println(uri);

        try {

            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            String output;

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            while ((output = br.readLine()) != null) {
                System.out.println(output);
                Object obj = JSONValue.parse(output);
                JSONObject json=(JSONObject)obj;
                JSONObject x = (JSONObject)json.get(name);
                System.out.println(json.get(name));
                Gson gson = new Gson();
               return gson.fromJson(x.toJSONString(), Summoner.class);

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
        Summoner summoner =  new Summoner();
        return summoner ;


    }
    public static void main(String [] args) throws InterruptedException, ExecutionException, TimeoutException {

      //  APISummoner s = new APISummoner("killermillergb");
        APISummoner.getIDapi("killermillergb");
    }
}
