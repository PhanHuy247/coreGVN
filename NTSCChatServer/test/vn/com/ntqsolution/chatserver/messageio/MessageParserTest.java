/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntqsolution.chatserver.messageio;

import com.vn.ntsc.chatserver.messageio.MessageParser;
import java.util.LinkedList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageType;

/**
 *
 * @author tuannxv00804
 */
public class MessageParserTest {
    
    public MessageParserTest() {
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
     * Test of serialize method, of class MessageParser.
     */
    @Test
    public void testSerialize() {
        Message msg = new Message( "tuanthitluoc", "sonto", MessageType.PP, "Chung ta can hoan thanh xong ChatServer truoc khi tuan nay ket thuc = moi gia, hoac la {moi thu se cham dut; the nhe';}" );
        String str = MessageParser.serialize( msg );
        
        //Test by eyes.
        System.out.println( str );
    }

    /**
     * Test of deserialize method, of class MessageParser.
     */
    @Test
    public void testDeserialize() {
        String msgStr = "{tuanthitluoc&sonto&20130619094319;tuanthitluoc;sonto;PP;Chung ta can hoan thanh xong ChatServer truoc khi tuan nay ket thuc = moi gia, hoac la &obmoi thu se cham dut&sc the nhe'&sc&cb;20130619094319; }";
        Message msg = MessageParser.deserialize( msgStr );
        System.out.println( msg );
    }

    /**
     * Test of parse method, of class MessageParser.
     */
    @Test
    public void testParse() {
        String msg1 = "{tuanthitluoc&sonto&20130619094319;tuanthitluoc;sonto;PP;Chung ta can hoan thanh xong ChatServer truoc khi tuan nay ket thuc = moi gia, hoac la &obmoi thu se cham dut&sc the nhe'&sc&cb;20130619094319; }";
        String msg2 = "{tuanthitluoc&sonto&20130619094319;tuanthitluoc2q;sonto2;PP;Chung ta can hoan thanh xoasdfasdf asdf asdf adsfkhi tuan nay ket thuc = moi gia, hoac la &obmoi thu se cham dut&sc the nhe'&sc&cb;20130619094319; }";
        
        String msgStr = msg1 + " nothing is impossible" + msg2 + "do something for fun.";
        
        StringBuilder builder = new StringBuilder( msgStr );
        LinkedList<Message> ll = MessageParser.parse( builder );
        
        System.out.println( "Remanin Str = " + builder.toString() + " {he he }");
        
        for( int i = 0; i < ll.size(); i++ ){
            System.out.println( ll.get( i ) );
        }
    }
}
