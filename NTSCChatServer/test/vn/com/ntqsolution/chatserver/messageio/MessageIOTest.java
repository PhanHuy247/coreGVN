/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntqsolution.chatserver.messageio;

import com.vn.ntsc.chatserver.messageio.MessageIO;
import java.net.Socket;
import java.util.LinkedList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.chatserver.pojos.user.UserConnection;

/**
 *
 * @author tuannxv00804
 */
public class MessageIOTest {
    
    public MessageIOTest() {
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
     * Test of sendMessage method, of class MessageIO.
     */
    @Test
    public void testSendMessage_Socket_String() {
        System.out.println( "sendMessage" );
        Socket soc = null;
        String msg = "";
        int expResult = 0;
        int result = MessageIO.sendMessage( soc, msg );
        assertEquals( expResult, result );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }

    /**
     * Test of sendMessage method, of class MessageIO.
     */
    @Test
    public void testSendMessage_Socket_Message() {
        System.out.println( "sendMessage" );
        Socket soc = null;
        Message msg = null;
        int expResult = 0;
        int result = MessageIO.sendMessage( soc, msg );
        assertEquals( expResult, result );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }

    /**
     * Test of sendMessage method, of class MessageIO.
     */
    @Test
    public void testSendMessage_UserConnection_Message() {
        System.out.println( "sendMessage" );
        UserConnection UC = null;
        Message msg = null;
        int expResult = 0;
        int result = MessageIO.sendMessage( UC, msg );
        assertEquals( expResult, result );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }

    /**
     * Test of isClientAlive method, of class MessageIO.
     */
    @Test
    public void testIsClientAlive() {
        System.out.println( "isClientAlive" );
        Socket soc = null;
        boolean expResult = false;
        boolean result = MessageIO.isClientAlive( soc );
        assertEquals( expResult, result );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }

    /**
     * Test of readMessage method, of class MessageIO.
     */
    @Test
    public void testReadMessage() {
        System.out.println( "readMessage" );
        Socket soc = null;
        LinkedList expResult = null;
        LinkedList result = MessageIO.readMessage( soc );
        assertEquals( expResult, result );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }

    /**
     * Test of readMessageToOutbox method, of class MessageIO.
     */
    @Test
    public void testReadMessageToOutbox() {
        System.out.println( "readMessageToOutbox" );
        UserConnection UC = null;
        MessageIO.readMessageToOutbox( UC );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }
}
