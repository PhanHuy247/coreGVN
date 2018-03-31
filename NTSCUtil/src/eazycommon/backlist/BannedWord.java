/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eazycommon.backlist;

import org.json.simple.JSONObject;

/**
 *
 * @author hungdt
 */
public class BannedWord implements IEntity{
    private static final String idKey = "id";
    public String id;

    private static final String wordKey = "word";
    public String word;

    private static final String flagKey = "flag";
    public Integer flag;    

    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        if(this.id != null)
            jo.put(idKey, this.id);
        if(this.word != null)
            jo.put(wordKey, this.word);
        if(this.flag != null)
            jo.put(flagKey, this.flag);

        return jo;
    }   

    public BannedWord(String id, String word, Integer flag) {
        this.id = id;
        this.word = word;
        this.flag = flag;
    }

}
