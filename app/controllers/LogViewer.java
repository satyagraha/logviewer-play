package controllers;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.logviewer.core.LogConfig;
import org.logviewer.core.LogManager;
import org.logviewer.core.MessageSender;
import org.logviewer.utility.LogConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import views.html.display;

public class LogViewer extends Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogViewer.class);

    public static Result display() {
        return ok(display.render());
    }

    private static class WSConfig implements LogConfig {
        
        private final LogConfigProperties properties = new LogConfigProperties();
        private final ExecutorService executorService = new ThreadPoolExecutor(0, 100, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(100, true));

        @Override
        public Executor getExecutor() {
            return executorService;
        }

        @Override
        public Properties getProperties() {
            return properties;
        }
    }

    private static class WSLogManager {

        public WSLogManager(WebSocket.In<String> in, final WebSocket.Out<String> out) {
            LOGGER.debug("constructor");
            
            final LogManager logManager = new LogManager(new WSConfig(), new MessageSender() {
                
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

    private static class WSHandler extends WebSocket<String> {

        // Called when the Websocket Handshake is done.
        @Override
        public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
            new WSLogManager(in, out);
        }

    }

    public static WebSocket<String> websocket() {

        return new WSHandler();

    }

}
