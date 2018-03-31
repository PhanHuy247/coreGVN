/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import eazycommon.util.Util;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import org.bson.types.ObjectId;
import com.vn.ntsc.usermanagementserver.Config;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.entity.impl.user.UserTemplate;

/**
 *
 * @author RuAc0n
 */
public class UserTemplateDAO {

    private static DBCollection coll;
    
    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.USER_TEMPLATE_COLLECTION);
            coll.createIndex(new BasicDBObject(UserdbKey.USER_TEMPLATE.USER_ID, 1));
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
           
        }
    }

    public static String add(UserTemplate template) throws EazyException {
        String id = null;
        try {
            BasicDBObject insertObj = new BasicDBObject(UserdbKey.USER_TEMPLATE.SORT_TIME, Util.currentTime());
            insertObj.put(UserdbKey.USER_TEMPLATE.USER_ID, template.userId);
            insertObj.put(UserdbKey.USER_TEMPLATE.TEMPLATE_CONTENT, template.templateContent);
            insertObj.put(UserdbKey.USER_TEMPLATE.TEMPLATE_TITLE, template.templateTitle);
            coll.insert(insertObj);
            id = insertObj.getObjectId(UserdbKey.USER_TEMPLATE.ID).toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return id;
    }
    
    public static void update(String id, UserTemplate template) throws EazyException {
        try {
            BasicDBObject findObj = new BasicDBObject(UserdbKey.USER_TEMPLATE.ID, new ObjectId(id));
            BasicDBObject updateObj = new BasicDBObject(UserdbKey.USER_TEMPLATE.SORT_TIME, Util.currentTime());
            updateObj.put(UserdbKey.USER_TEMPLATE.USER_ID, template.userId);
            updateObj.put(UserdbKey.USER_TEMPLATE.TEMPLATE_CONTENT, template.templateContent);
            updateObj.put(UserdbKey.USER_TEMPLATE.TEMPLATE_TITLE, template.templateTitle);
            coll.update(findObj, updateObj);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }
    
    public static void remove(String id) throws EazyException {
        try {
            BasicDBObject findObj = new BasicDBObject(UserdbKey.USER_TEMPLATE.ID, new ObjectId(id));
            coll.remove(findObj);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }
    
    private static final BasicDBObject sortByTimeDesc = new BasicDBObject(UserdbKey.USER_TEMPLATE.SORT_TIME, -1);
    public static List<UserTemplate> list(String userId) throws EazyException {
        List<UserTemplate> templates = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject(UserdbKey.USER_TEMPLATE.USER_ID, userId);
            DBCursor cursor = coll.find(findObj).sort(sortByTimeDesc).limit(Config.MAX_USER_TEMPLATE_NUMBER);
            while(cursor.hasNext()){
                DBObject obj = cursor.next();
                String templateContent = (String) obj.get(UserdbKey.USER_TEMPLATE.TEMPLATE_CONTENT);
                String templateId = obj.get(UserdbKey.USER_TEMPLATE.ID).toString();
                String templateTitle = (String) obj.get(UserdbKey.USER_TEMPLATE.TEMPLATE_TITLE);
                templates.add(new UserTemplate(templateId, userId, templateContent, templateTitle));
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return templates;
    }

}
