package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import views.html.display;
import controllers.logviewer.support.WebSocketLogHandler;

public class LogViewer extends Controller {

    public static Result display() {
        return ok(display.render());
    }

    public static WebSocket<String> websocket() {
        return new WebSocketLogHandler();
    }

}
