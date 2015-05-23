package api;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.Gson;
import java.util.Collection;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.lang.reflect.Type;
import java.lang.reflect.*;
import java.lang.reflect.TypeVariable;
/**
 * Created by Thomas on 22/05/2015.
 */
public class json {



    private int data1 = 100;
    private String data2 = "hello";
    private String matche;
    private List<JSONObject> matches = new ArrayList<JSONObject>() {};



    @Override
    public String toString() {
        return "DataObject [matche=" + data1 + ", data2=" + data2 + ", data3=" + matche + ", list="
                + matches + "]";
    }
}

class MatchHistory {

    private List<Match> list = new ArrayList<Match>() {
        {

        }
    };


    private String matche = "";
    @Override
    public String toString() {
        return "test "+matche;
    }
}


class GsonExample {
    public static void main(String[] args) {

        Gson gson = new Gson();

        try {

            BufferedReader br = new BufferedReader(
                    new FileReader("d:\\file.json"));

            //convert the json string back to object
            //MatchHistory obj = gson.fromJson(br, MatchHistory.class);
            //Object obj = JSONValue.parse(br);
            //JSONObject json=(JSONObject)obj;
            //JSONArray matches=(JSONArray ) json.get("matches");
            json obj = gson.fromJson(br, json.class);


            System.out.println(obj);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
