package controllers.logviewer.support;

import java.io.IOException;

import org.logviewer.core.LogManager;
import org.logviewer.services.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.mvc.WebSocket;
import controllers.LogViewer;

public class WebSocketLogManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogViewer.class);

    public WebSocketLogManager(WebSocket.In<String> in, final WebSocket.Out<String> out) {
        LOGGER.debug("constructor");

        final LogManager logManager = new LogManager(new WebSocketLogConfig(), new MessageSender() {

            @Override
            public void sendMessage(String message) throws IOException {
                out.write(message);
            }
        });

        // For each event received on the socket,
        in.onMessage(new Callback<String>() {

            @Override
            public void invoke(String event) {
                try {
                    logManager.handleMessage(event);
                } catch (IOException e) {
                    LOGGER.error("unexpected exception", e);
                }
            }
        });

        // When the socket is closed.
        in.onClose(new Callback0() {

            @Override
            public void invoke() {
                logManager.close();
            }
        });

    }

}