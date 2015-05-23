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
import java.lang.reflect.Array;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Thomas on 21/05/2015.
 */

public class main {
    public Gson gson = new Gson();
    private static final int MYTHREADS = 10;

    public static void main(String [] args){
        ExecutorService executor = Executors.newFixedThreadPool(MYTHREADS);
        String prefex = "https://euw.api.pvp.net";
        String sufex = "/api/lol/euw/v2.2/matchhistory/35080577";
        String key = "2d33b014-b236-4d80-88e4-60567ae5026c";
        String url = prefex+sufex+"?api_key="+key;

        String beginIndex,endIndex;
        main main = new main();

        String[] urls = new String[10];
        for(int i = 0; i<10; i++) {
            int x = i *10;
            beginIndex= Integer.toString(x);
            endIndex= Integer.toString(x+10);
            urls[i] = url +"&beginIndex="+beginIndex+"&endIndex="+endIndex;
            //main.makeCall(urls[i]);
            //main.makeCall(urls[i]);
            Runnable worker = new MyRunnable(urls[i]);
            executor.execute(worker);

        }
        while (!executor.isTerminated()) {

        }
        System.out.println("\nFinished all threads");


       // System.out.println(json);
    }

    public static class MyRunnable implements Runnable {
        private final String url;

        MyRunnable(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            try {

                URL url = new URL(this.url);
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
                    //return null;

                }

                conn.disconnect();

            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }
        }
    }



    private static String getMatchObject(JSONObject json){

        //System.out.println(json);
        JSONArray  matches=(JSONArray ) json.get("matches");
        for(int i = 0; i<10; i++) {
            JSONObject matchJSON = (JSONObject) matches.get(i);
            JSONArray participants = (JSONArray) matchJSON.get("participants");
            JSONObject participant = (JSONObject) participants.get(0);
            JSONObject statsJSON = (JSONObject) participant.get("stats");
            JSONObject timelineJSON = (JSONObject) participant.get("timeline");
            Gson gson = new Gson();

            Match match = gson.fromJson(matchJSON.toJSONString(), Match.class);
            Stats stats = gson.fromJson(statsJSON.toJSONString(), Stats.class);
            Timeline timeline = gson.fromJson(timelineJSON.toJSONString(), Timeline.class);
            System.out.println(timeline.getLane());
            match.stats = stats;
        }
        return null;

    }

}

class Participant{
    public Stats stats;
    //public Timeline timeline;
}
class Timeline{

    public static class Time {
        private float thirtyToEnd, twentyToThirty, tenToTwenty, zeroToTen;

        public float getZeroToTen() { return zeroToTen; }
        public float getTenToTwenty(){ return tenToTwenty; }
        public float getTwentyToThirty() { return twentyToThirty; }
        public float getThirtyToEnd(){ return thirtyToEnd; }

        public void setZeroToTen      (float i){ zeroToTen = i;}
        public void setTenToTwenty    (float i){ tenToTwenty = i;}
        public void setTwentyToThirty (float i){ twentyToThirty = i;}
        public void setThirtyToEnd    (float i){ thirtyToEnd = i;}
    }
    public static class CsDiffPerMinDeltas extends Time {}
    public static class DamageTakenPerMinDeltas extends Time {}
    public static class DamageTakenDiffPerMinDeltas extends Time {}
    public static class XpPerMinDeltas extends Time {}
    public static class XpDiffPerMinDeltas extends Time {}
    public static class CreepsPerMinDeltas extends Time {}
    public static class GoldPerMinDeltas extends Time {}


    public CsDiffPerMinDeltas csDiffPerMinDeltas;
    public DamageTakenPerMinDeltas damageTakenPerMinDeltas;
    private XpPerMinDeltas xpPerMinDeltas;
    private XpDiffPerMinDeltas xpDiffPerMinDeltas;
    private CreepsPerMinDeltas creepsPerMinDeltas;
    private GoldPerMinDeltas goldPerMinDeltas;
    private Enums.role role;
    private Enums.lane lane;


    public CsDiffPerMinDeltas      getCsDiffPerMinDeltas(){return csDiffPerMinDeltas;}
    public DamageTakenPerMinDeltas getDamageTakenPerMinDeltas(){return damageTakenPerMinDeltas;}
    public XpPerMinDeltas          getXpPerMinDeltas(){return xpPerMinDeltas;}
    public XpDiffPerMinDeltas      getXpDiffPerMinDeltas(){return xpDiffPerMinDeltas;}
    public CreepsPerMinDeltas      getCreepsPerMinDeltas(){return creepsPerMinDeltas;}
    public GoldPerMinDeltas        getGoldPerMinDeltas(){return goldPerMinDeltas;}
    public Enums.role              getRole(){return role;}
    public Enums.lane              getLane(){return lane;}

    public void setDamageTakenPerMinDeltas (CsDiffPerMinDeltas       cs) { csDiffPerMinDeltas = cs; }
    public void setDamageTakenPerMinDeltas (DamageTakenPerMinDeltas  cs) { damageTakenPerMinDeltas = cs; }
    public void setXpPerMinDeltas          (XpPerMinDeltas           cs) { xpPerMinDeltas          = cs; }
    public void setXpDiffPerMinDeltas      (XpDiffPerMinDeltas       cs) { xpDiffPerMinDeltas      = cs; }
    public void setCreepsPerMinDeltas      (CreepsPerMinDeltas       cs) { creepsPerMinDeltas      = cs; }
    public void setGoldPerMinDeltas        (GoldPerMinDeltas         cs) { goldPerMinDeltas        = cs; }
    public void setRole                    (Enums.role role) { this.role = role; }
    public void setLane                    (Enums.lane lane) { this.lane = lane; }

}
class LolTime{
    public float thirtyToEnd;
    public float twentyToThirty;
    public float tenToTwenty;
    public float zeroToTen;


}
class Match{
    public Stats stats;


    public String platformId;
    public long matchCreation;
    public int matchDuration;
    public String queueType;
    public String season;
    public int mapId;
    public String region;
    public long matchId;
    public String matchMode;
    //public Participant participants;


}
class Stats{

    public int item0;
    public int item2;
    public int totalUnitsHealed;
    public int item1;
    public int towerKills;
    public int largestMultiKill;
    public int goldEarned;
    public boolean firstInhibitorKill;
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
    public boolean firstTowerKill;
    public int magicDamageDealt;
    public int totalScoreRank;
    public boolean winner;

    public int wardsPlaced;
    public int totalDamageDealt;
    public int largestKillingSpree;
    public int totalDamageDealtToChampions;
    public int physicalDamageDealtToChampions;
    public int neutralMinionsKilledTeamJungle;

    public boolean firstInhibitorAssist;
    public int objectivePlayerScore;
    public int visionWardsBoughtInGame;
    public int kills;
    public int minionsKilled;
    public boolean firstTowerAssist;
    public int combatPlayerScore;
    public int inhibitorKills;
    public int trueDamageTaken;
    public boolean firstBloodAssist;
    public int assists;
    public int goldSpent;
    public int totalHeal;
    public int unrealKills;
    public int physicalDamageDealt;
    public boolean firstBloodKill;
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

    public int getKills(){
        return kills;
    }

    @Override
    public String toString() {
        return ""+kills;
    }
}
