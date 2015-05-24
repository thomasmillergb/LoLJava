package api;


import java.util.*;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.codehaus.jackson.type.TypeReference;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import com.google.gson.Gson;

import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * Created by Thomas on 21/05/2015.
 */

public class main {
    public Gson gson = new Gson();
    private static final int MYTHREADS = 10;

    public static void main(String [] args) throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executor = Executors.newFixedThreadPool(MYTHREADS);
        String prefex = "https://euw.api.pvp.net";
        String sufex = "/api/lol/euw/v2.2/matchhistory/35080577";
        String key = "2d33b014-b236-4d80-88e4-60567ae5026c";
        String url = prefex+sufex+"?api_key="+key;

        String beginIndex,endIndex;
        main main = new main();
        List<String> urls= new ArrayList<>() ;
        IntStream
                .range(0, 10)
                .forEach(i -> urls.add(url +"&beginIndex="+i*10+"&endIndex="+i*10+10));
        MultithreadingAPIcalls m = new MultithreadingAPIcalls();
        m.run(urls);

    }

}
class MultithreadingAPIcalls {
    //
    public void run(List<String>urls) throws InterruptedException, ExecutionException, TimeoutException {

        //List<Callable<Map<Integer,Match>>> callables =new ArrayList<Callable<Map<Integer,Match>>>(){};
        List<Map<Long,Match>> matches =new ArrayList<Map<Long,Match>>(){};

        //urls.forEach(f -> callables.add((Callable<Map<Integer, Match>>) getMatchHistory(f)));

        /*
        List<Callable<Map<Long,Match>>> callables = Arrays.asList(
                () -> getMatchHistory(urls.get(1)),
                () -> getMatchHistory(urls.get(2)),
                () -> getMatchHistory(urls.get(3)));
        */
        /*
        List<Callable<Map<Long,Match>>> callables = Arrays.asList(
                () -> urls.stream());
        */
        /*
        List<Callable<Map<Long,Match>>> callables = Arrays.asList(
                .range(1, 10)
                () -> getMatchHistory(urls.get(1)),
        */
        List<Callable<Map<Long, Match>>> callables = new ArrayList<Callable<Map<Long, Match>>>(){};
        urls.forEach(f -> callables.add(() -> getMatchHistory(f)));
        ExecutorService executor = Executors.newWorkStealingPool();
        executor.invokeAll(callables)
                .stream()
                .map(future -> {
                    try {
                        return future.get();
                    }
                    catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                })
                .forEach(i ->matches.add(i));
        //.forEach(System.out::println);
        System.out.println(matches);

    }
    public int test(int i ){
        return i;

    }
    public Map<Long,Match> getMatchHistory(String uri) {
        Map<Long,Match> map = new HashMap<Long,Match>();
        try {

            URL url = new URL(uri);
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
            // System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                Object obj = JSONValue.parse(output);
                JSONObject json=(JSONObject)obj;

                JSONArray  matches=(JSONArray ) json.get("matches");
                //System.out.println("Output from Server .... \n");

                for(int i = 0; i<10; i++) {
                    Match match = getMatchObject((JSONObject) matches.get(i));
                    map.put(match.matchId,match);
                }
            }
            conn.disconnect();

        } catch (MalformedURLException e) {
        }
        finally {
            return map;
        }

    }
    private static Match getMatchObject(JSONObject matchJSON){

        //System.out.println(json);


        JSONArray participants = (JSONArray) matchJSON.get("participants");
        JSONObject participant = (JSONObject) participants.get(0);
        JSONObject statsJSON = (JSONObject) participant.get("stats");
        JSONObject timelineJSON = (JSONObject) participant.get("timeline");
        Gson gson = new Gson();

        Match match = gson.fromJson(matchJSON.toJSONString(), Match.class);
        Stats stats = gson.fromJson(statsJSON.toJSONString(), Stats.class);
        Timeline timeline = gson.fromJson(timelineJSON.toJSONString(), Timeline.class);
        System.out.println(timeline.getCsDiffPerMinDeltas().getZeroToTen());
        match.stats = HashJSON(statsJSON);


        // HashMap<String,Object> result =  new ObjectMapper().readValue(statsJSON.toJSONString(), HashMap.class);

        return match;

    }
    public static Map<String,String> HashJSON(JSONObject json){

        //String json = "{\"name\":\"mkyong\", \"age\":\"29\"}";

        Map<String,String> map = new HashMap<String,String>();
        ObjectMapper mapper = new ObjectMapper();

        try {

            //convert JSON string to Map
            map = mapper.readValue(json.toJSONString(),
                    new TypeReference<HashMap<String,String>>(){});

            //System.out.println(map);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(map.get("kills"));
        return map;
    }

}
class Match{
    public Map<String, String> stats;
    public Timeline timeline;

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


    private CsDiffPerMinDeltas csDiffPerMinDeltas;
    private DamageTakenPerMinDeltas damageTakenPerMinDeltas;
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
