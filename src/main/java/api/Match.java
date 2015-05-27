package api;

import java.util.Map;

/**
 * Created by Thomas on 27/05/2015.
 */
public class Match {

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

        @Override
        public String toString(){
            return  "Kills:" +stats.get("kills")+ " matchDuration"+ matchDuration;
        }
        public Match(){
                
        }
}
