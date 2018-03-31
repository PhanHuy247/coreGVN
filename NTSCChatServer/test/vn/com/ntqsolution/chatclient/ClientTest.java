/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntqsolution.chatclient;

import com.vn.ntsc.chatclient.Client;
import java.util.Date;
import java.util.LinkedList;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.vn.ntsc.Config;
import com.vn.ntsc.chatclient.listeners.IAuthenListener;
import com.vn.ntsc.chatclient.listeners.IChatListener;
import com.vn.ntsc.chatclient.listeners.IGenericMessagesListener;
import com.vn.ntsc.chatclient.listeners.IMessageStatusListener;
import com.vn.ntsc.chatclient.listeners.IPresenceListener;
import com.vn.ntsc.chatserver.pojos.message.Message;

/**
 *
 * @author tuannxv00804
 */
public class ClientTest {
    
    public ClientTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of sendAuthenMessage method, of class Client.
     */
    //@Test
    public void testSendAuthenMessage() {
        System.out.println( "sendAuthenMessage" );
        String username = "";
        String password = "";
        Client instance = null;
        instance.sendAuthenMessage( username, password );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }

    /**
     * Test of sendMessage method, of class Client.
     */
    //@Test
    public void testSendMessage() {
        System.out.println( "sendMessage" );
        Message message = null;
        Client instance = null;
        boolean expResult = false;
        boolean result = instance.sendMessage( message );
        assertEquals( expResult, result );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }

    /**
     * Test of markMessageAsRead method, of class Client.
     */
    //@Test
    public void testMarkMessageAsRead() {
        System.out.println( "markMessageAsRead" );
        String messageid = "";
        String toUsername = "";
        Client instance = null;
        instance.markMessageAsRead( messageid );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }

    /**
     * Test of addGenericMessageListener method, of class Client.
     */
    //@Test
    public void testAddGenericMessageListener() {
        System.out.println( "addGenericMessageListener" );
        IGenericMessagesListener msgListener = null;
        Client instance = null;
        instance.addGenericMessageListener( msgListener );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }

    /**
     * Test of addAuthenListener method, of class Client.
     */
    //@Test
    public void testAddAuthenListener() {
        System.out.println( "addAuthenListener" );
        IGenericMessagesListener authenLitener = null;
        Client instance = null;
        instance.addAuthenListener( (IAuthenListener) authenLitener);
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }

    /**
     * Test of addChatListener method, of class Client.
     */
    //@Test
    public void testAddChatListener() {
        System.out.println( "addChatListener" );
        IChatListener chatListener = null;
        Client instance = null;
        instance.addChatListener( chatListener );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }

    /**
     * Test of addPresenceListener method, of class Client.
     */
    //@Test
    public void testAddPresenceListener() {
        System.out.println( "addPresenceListener" );
        IPresenceListener presenceListener = null;
        Client instance = null;
        instance.addPresenceListener( presenceListener );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }

    /**
     * Test of addMessgeStatusListener method, of class Client.
     */
    //@Test
    public void testAddMessgeStatusListener() {
        System.out.println( "addMessgeStatusListener" );
        IGenericMessagesListener messageStatusListener = null;
        Client instance = null;
        instance.addMessgeStatusListener( (IMessageStatusListener) messageStatusListener);
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }

    /**
     * Test of getLogMsgInbox method, of class Client.
     */
    //@Test
    public void testGetLogMsgInbox() {
        System.out.println( "getLogMsgInbox" );
        Date fromDate = null;
        String fromUser = "";
        Client instance = null;
        LinkedList expResult = null;
        LinkedList result = instance.getLogMsgInbox( fromDate, fromUser );
        assertEquals( expResult, result );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }

    /**
     * Test of getLogMsgOutbox method, of class Client.
     */
    //@Test
    public void testGetLogMsgOutbox() {
        System.out.println( "getLogMsgOutbox" );
        Date fromDate = null;
        String toUser = "";
        Client instance = null;
        LinkedList expResult = null;
        LinkedList result = instance.getLogMsgOutbox( fromDate, toUser );
        assertEquals( expResult, result );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }

    /**
     * Test of getOfflineMessage method, of class Client.
     */
    @Test
    public void testGetOfflineMessage() {
        System.out.println( "getOfflineMessage testing" );
        Client instance = new Client( Config.ChatServerIP, Config.ChatServerPort );
        instance.sendAuthenMessage( "tuanthitluoc", "thientai" );
        
        LinkedList<Message> ll = instance.getOfflineMessage();
        for( int i = 0; i < ll.size(); i++ ){
            System.out.println( ll.get( i ) );
        }
    }

    /**
     * Test of requestFriendStatus method, of class Client.
     */
    //@Test
    public void testRequestFriendStatus() {
        System.out.println( "requestFriendStatus" );
        String friendUsername = "";
        Client instance = null;
        instance.requestFriendStatus( friendUsername );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }

    /**
     * Test of requestFriendListStatus method, of class Client.
     */
    //@Test
    public void testRequestFriendListStatus() {
        System.out.println( "requestFriendListStatus" );
        Client instance = null;
        instance.requestFriendListStatus();
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }

    /**
     * Test of requestAccquaintanceListStatus method, of class Client.
     */
    //@Test
    public void testRequestAccquaintanceListStatus() {
        System.out.println( "requestAccquaintanceListStatus" );
        Client instance = null;
        instance.requestAccquaintanceListStatus();
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }

    /**
     * Test of setStatus method, of class Client.
     */
    //@Test
    public void testSetStatus() {
        System.out.println( "setStatus" );
        String status = "";
        Client instance = null;
        instance.setStatus( status );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }

    /**
     * Test of sendWritingSignal method, of class Client.
     */
    //@Test
    public void testSendWritingSignal() {
        System.out.println( "sendWritingSignal" );
        String friendUsername = "";
        Client instance = null;
        instance.sendWritingSignal( friendUsername );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }

    /**
     * Test of sendStopWritingSignal method, of class Client.
     */
    //@Test
    public void testSendStopWritingSignal() {
        System.out.println( "sendStopWritingSignal" );
        String friendUsername = "";
        Client instance = null;
        instance.sendStopWritingSignal( friendUsername );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }

    /**
     * Test of sendErasingSignal method, of class Client.
     */
    //@Test
    public void testSendErasingSignal() {
        System.out.println( "sendErasingSignal" );
        String friendUsername = "";
        Client instance = null;
        instance.sendErasingSignal( friendUsername );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }

    /**
     * Test of wink method, of class Client.
     */
    //@Test
    public void testWink_String() {
        System.out.println( "wink" );
        String friendUsername = "";
        Client instance = null;
        instance.wink( friendUsername );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }

    /**
     * Test of wink method, of class Client.
     */
    //@Test
    public void testWink_long() {
        System.out.println( "wink" );
        long percentOfTotalUser = 0L;
        Client instance = null;
        instance.wink( percentOfTotalUser );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }
}
