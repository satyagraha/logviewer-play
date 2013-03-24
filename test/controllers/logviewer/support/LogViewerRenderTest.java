package controllers.logviewer.support;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;

import org.junit.Test;

import play.mvc.Content;

/**
*
*/
public class LogViewerRenderTest {

    @Test
    public void renderTemplate() {
        Content html = views.html.display.render();
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("<title>Log File Viewer</title>");
    }

}
