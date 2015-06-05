package api.JSONobjects;

/**
 * Created by Thomas on 31/05/2015.
 */
public class Summoner {
    public String name;
    public int id;
    public int profileIconId;
    public int summonerLevel;
    public long revisionDate;
    public Summoner(){

    }
    public Summoner(String name, int id){
        this.id = id;
        this.name = name;

    }
    @Override
    public String toString(){
        return "SummornerName: "+name+" ID: "+id;
    }
}
