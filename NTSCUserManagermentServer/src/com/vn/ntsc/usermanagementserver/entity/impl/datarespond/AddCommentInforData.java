/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.datarespond;

import java.util.Collection;
import java.util.List;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class AddCommentInforData implements IEntity {

    public Integer point;
    
    private static final String subCommentPointKey = "sub_comment_point";
    public Integer subCommentPoint;
    
    private static final String commentBuzzPointKey = "comment_buzz_point";
    public Integer commentBuzzPoint;
    
    private static final String blockListKey = "block_lst";
    public List<String> block_lst;

    private static final String deactiveListKey = "deact_lst";
    public Collection<String> deactiveList;

    public AddCommentInforData(List<String> block_lst, Collection<String> deactiveList, int point, int commentBuzzPoint) {
        this.block_lst = block_lst;
        this.deactiveList = deactiveList;
        this.point = point;
        this.commentBuzzPoint = commentBuzzPoint;
    }
    
    public AddCommentInforData(int subCommentPoint, List<String> block_lst, Collection<String> deactiveList, int point) {
        this.block_lst = block_lst;
        this.deactiveList = deactiveList;
        this.point = point;
        this.subCommentPoint = subCommentPoint;
    }
    
    public AddCommentInforData(List<String> block_lst, Collection<String> deactiveList, int point) {
        this.block_lst = block_lst;
        this.deactiveList = deactiveList;
        this.point = point;
    }
    
    public AddCommentInforData( int point, Integer commentBuzzPoint, Integer subcommentPoint) {
        this.point = point;
        this.commentBuzzPoint = commentBuzzPoint;
        this.subCommentPoint = subcommentPoint;
    }    
    
    public AddCommentInforData( int point) {
        this.point = point;
    }    

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        jo.put(ParamKey.POINT, point);
        
        if(subCommentPoint != null)
            jo.put(subCommentPointKey, subCommentPoint);
        if(commentBuzzPoint != null)
            jo.put(commentBuzzPointKey, commentBuzzPoint);
        
        if (this.block_lst != null) {
            JSONArray arr = new JSONArray();
            for (String block_lst1 : this.block_lst) {
                arr.add(block_lst1);
            }
            jo.put(blockListKey, arr);
        }
        if (this.deactiveList != null) {
            JSONArray arr = new JSONArray();
            for (String deactiveList1 : this.deactiveList) {
                arr.add(deactiveList1);
            }
            jo.put(deactiveListKey, arr);
        }
        return jo;
    }
}
