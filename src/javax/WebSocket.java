package javax;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.sound.DetectSilence;

@ServerEndpoint("/audio-api")
public class WebSocket {	
    @OnMessage
    public void echoTextMessage(Session session, String msg, boolean last) {
        try {
            if (session.isOpen()) {
                session.getBasicRemote().sendText(msg, last);
            }
        } catch (IOException e) {
            try {
                session.close();
            } catch (IOException e1) {
                // Ignore
            }
        }
    }
    
    @OnError
    public void onError(Throwable e){
        e.printStackTrace();
    }


}