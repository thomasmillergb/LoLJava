package mongoDB;

import api.Match;
import api.MatchHistory;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.bson.Document;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;

/**
 * Java MongoDB : Insert a Document
 *
 */



public class Connect {
    private  MongoClient mongo;
    private  MongoDatabase database;
    private  String lolID;
    private  MongoCollection<Document> dbCollection;
    public Connect(String lolID){
        this.lolID = lolID;
        mongo = new MongoClient("localhost", 27017);
        database = mongo.getDatabase("League_of_Legends");
        //Match_History
        dbCollection = database.getCollection("Match_History");

    }
    public void insert(){
       // MongoCollection<Document> dbCollection = database.getCollection("test2");

        MatchHistory mh = new MatchHistory(lolID);
        Gson gson = new Gson();
        String json = gson.toJson(mh.get());
        Document d = new Document();
        d.put("_id", lolID);
        d.put("match",  Document.parse(json));

        BasicDBObject updateQuery = new BasicDBObject();
        updateQuery.put("_id", lolID);

        BasicDBObject updateCommand = new BasicDBObject();
        updateCommand.put( "$set", new BasicDBObject("match",  Document.parse(json)  ));

        try {
            dbCollection.insertOne(d);
        } catch (MongoWriteException e) {

            dbCollection.updateOne(updateQuery, updateCommand);
            System.out.println("yay");
        }
        mongo.close();

    }
    public JSONObject getMatchHistory(){
        Map<Long,DataFormatReaders.Match> matchTree = new TreeMap<>();
        String json = findDocumentById().first().toJson();
        Object obj = JSONValue.parse(json);
        JSONObject json2=(JSONObject)obj;
        JSONObject participant = (JSONObject) json2.get("match");

        return participant;
        /*
        System.out.print(json2);
        HashJSON(participant);
        matchTree.putAll(participant);

        Type typeOfHashMap = new TypeToken<Map<Long, Match>>() { }.getType();
        Gson gson = new Gson();
        participant.entrySet().stream().forEach(System.out::println);

*/


        //Type typeOfHashMap = new TypeToken<Map<Long, Match>>() { }.getType();
        //Gson gson = new Gson();
        //Map<Long, Match> newMap = gson.fromJson(participant.toJSONString(), typeOfHashMap);


    }
    public MongoIterable<Document> findDocumentById() {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", lolID);

        //MongoIterable<Document> dbObj = dbCollection.find(query).limit(1);
        MongoIterable<Document> dbObj = dbCollection.find(new Document("_id", lolID)).limit(1);
        return dbObj;
    }
    public static Map<String,String> HashJSON(JSONObject json){



        Map<String,String> map = new TreeMap<String,String>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            //convert JSON string to Map
            map = mapper.readValue(json.toJSONString(),
                    new TypeReference<TreeMap<String,String>>(){});

            //System.out.println(map);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(map.get("kills"));
        return map;
    }
    public static void main(String[] args) {

        String lolID = "35080577";
        Connect mongo = new Connect(lolID);
        //mongo.insert();
        mongo.getMatchHistory();


    }


}