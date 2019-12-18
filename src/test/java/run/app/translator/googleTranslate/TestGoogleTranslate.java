package run.app.translator.googleTranslate;

import org.junit.Test;

import java.io.IOException;

public class TestGoogleTranslate {
    private GoogleTranslate googleTranslate = new GoogleTranslate();

    @Test
    public void testGetTTk() {
        try {
            String ttk = googleTranslate.getTKK();
            System.out.println(ttk);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
