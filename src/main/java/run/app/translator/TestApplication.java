package run.app.translator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestApplication {
    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("\"(.*?)\"[\\s]*=[\\s]*\"(.*?)\"[\\s]*;");
        Matcher matcher = pattern.matcher("\"Relatived Works\" = \"相关作品\";");
        if(matcher.find()) {
            String result = matcher.group(0);
            String key = matcher.group(1);
            String value = matcher.group(2);
            System.out.println("key:" + key + "; value:" + value);
        }
    }
}
