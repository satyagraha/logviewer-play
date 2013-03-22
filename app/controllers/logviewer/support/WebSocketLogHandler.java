package controllers.logviewer.support;

import play.mvc.WebSocket;

public class WebSocketLogHandler extends WebSocket<String> {

    // Called when the Websocket Handshake is done.
    @Override
    public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
        new WebSocketLogManager(in, out);
    }

}