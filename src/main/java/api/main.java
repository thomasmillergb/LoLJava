package api;


import jdk.nashorn.internal.objects.NativeJSON;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.lang.reflect.Type;
import java.util.Objects;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import org.json.simple.parser.JSONParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
/**
 * Created by Thomas on 21/05/2015.
 */

public class main {
    public Gson gson = new Gson();
    public static void main(String [] args){
        String prefex = "https://euw.api.pvp.net";
        String sufex = "/api/lol/euw/v2.2/matchhistory/35080577";
        String key = "2d33b014-b236-4d80-88e4-60567ae5026c";
        String url = prefex+sufex+"?api_key="+key;
        main main = new main();
        main.makeCall(url);

       // System.out.println(json);
    }
    public main(){

    }
    public JSONObject makeCall(String urll){

        try {

            URL url = new URL(urll);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                Object obj = JSONValue.parse(output);
                JSONObject json=(JSONObject)obj;
                getMatchObject(json);

                //JSONObject json = (JSONObject) new JSONParser().parse(output);
                //System.out.println(output);
                return null;

            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return null;
    }

    private String getMatchObject(JSONObject json){

        //System.out.println(json);
        JSONArray  matches=(JSONArray ) json.get("matches");
        JSONObject match=(JSONObject)matches.get(0);
        JSONArray  participants=(JSONArray ) match.get("participants");
        JSONObject participant=(JSONObject)participants.get(0);
        System.out.println(participant.get("stats"));

        Gson gson = new Gson();
        //GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.registerTypeAdapter(Stats.class, new StatsInstanceCreator());
        //Gson gson = gsonBuilder.create();
        JSONObject stats= participant.get("stats");
        Stats obj = gson.fromJson(stats.toJSONString(), participant.class);





        System.out.println(participant);
        return null;

    }

}
/*
class StatsInstanceCreator implements InstanceCreator<Stats> {
    public Stats createInstance(Type type)
    {
        return new Stats("None");
    }
}*/

class participant{
    public Stats stats;
    //public Timeline timeline;
}
class Timeline{
    public LolTime csDiffPerMinDeltas;
    public LolTime damageTakenPerMinDeltas;
    public LolTime damageTakenDiffPerMinDeltas;
    public LolTime xpPerMinDeltas;
    public LolTime xpDiffPerMinDeltas;
    public LolTime creepsPerMinDeltas;
    public LolTime goldPerMinDeltas;
}
class LolTime{
    public float thirtyToEnd;
    public float twentyToThirty;
    public float tenToTwenty;
    public float zeroToTen;

}
class Stats{

    public int item0;
    public int item2;
    public int totalUnitsHealed;
    public int item1;
    public int towerKills;
    public int largestMultiKill;
    public int goldEarned;
    public int firstInhibitorKill;
    public int physicalDamageTaken;
    public int totalPlayerScore;
    public int champLevel;
    public int totalDamageTaken;
    public int magicDamageTaken;
    public int neutralMinionsKilled;
    public int deaths;
    public int tripleKills;
    public int magicDamageDealtToChampions;
    public int wardsKilled;
    public int pentaKills;
    public int largestCriticalStrike;
    public int totalTimeCrowdControlDealt;
    public int firstTowerKill;
    public int magicDamageDealt;
    public int totalScoreRank;
    public int winner;
    public int wardsPlaced;
    public int totalDamageDealt;
    public int largestKillingSpree;
    public int totalDamageDealtToChampions;
    public int physicalDamageDealtToChampions;
    public int neutralMinionsKilledTeamJungle;
    public int firstInhibitorAssist;
    public int objectivePlayerScore;
    public int visionWardsBoughtInGame;
    public int kills;
    public int minionsKilled;
    public int firstTowerAssist;
    public int combatPlayerScore;
    public int inhibitorKills;
    public int trueDamageTaken;
    public int firstBloodAssist;
    public int assists;
    public int goldSpent;
    public int totalHeal;
    public int unrealKills;
    public int physicalDamageDealt;
    public int firstBloodKill;
    public int sightWardsBoughtInGame;
    public int killingSprees;
    public int neutralMinionsKilledEnemyJungle;
    public int trueDamageDealtToChampions;
    public int doubleKills;
    public int trueDamageDealt;
    public int quadraKills;
    public int item4;
    public int item3;
    public int item6;
    public int item5;


}
