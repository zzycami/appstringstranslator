package run.app.translator.googleTranslate;

import com.google.gson.Gson;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import okhttp3.*;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GoogleTranslate {
    private ScriptEngine scriptEngine;

    private String ttk;

    public void setScriptResource(Resource scriptResource) {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        scriptEngine = scriptEngineManager.getEngineByName("javascript");
        Reader scriptReader;
        try {
            scriptReader = new InputStreamReader(scriptResource.getInputStream(), StandardCharsets.UTF_8);
            scriptEngine.eval(scriptReader);
            scriptReader.close();
        } catch (IOException | ScriptException e) {
            e.printStackTrace();
        }
    }

    private static Headers browserHeaders() {
        return new Headers.Builder()
                .add("content-type", "application/x-www-form-urlencoded")
                .add("Accept", "application/json, text/plain, */*")
                .add("X-Requested-With", "XMLHttpRequest")
                .add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36")
                .build();
    }

    public String getTKK() throws IOException {
        if (!StringUtils.isEmpty(this.ttk)) {
            return this.ttk;
        }
        String url = "https://translate.google.cn/";
        String htmlValue = getResponse(url);
        if (!StringUtils.isEmpty(htmlValue)) {
            if (htmlValue.contains("tkk")) {
                Pattern pattern = Pattern.compile("tkk:'(.*?)',");
                Matcher matcher = pattern.matcher(htmlValue);
                if (matcher.find()) {
                    this.ttk = matcher.group(1);
                    return this.ttk;
                }
            }
        }
        return null;
    }

    private String getResponse(String url) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .url(url)
                .headers(browserHeaders())
                .build();
        Call requestCall = client.newCall(request);
        return Objects.requireNonNull(requestCall.execute().body()).string();
    }

    private String getToken(String word, String tkk) throws ScriptException, NoSuchMethodException {
        String result = null;
        if (scriptEngine != null) {
            if (scriptEngine instanceof Invocable) {
                Invocable invocable = (Invocable) scriptEngine;
                result = (String) invocable.invokeFunction("tk", new Object[]{word, tkk});
            }
        }
        return result;
    }

    public String translate(String word, String from, String to) throws IOException, ScriptException, NoSuchMethodException {
        if (StringUtils.isEmpty(word)) {
            return null;
        }
        String ttk = getTKK();
        if (StringUtils.isEmpty(ttk)) {
            return null;
        }
        String token = getToken(word, ttk);
        word = URLEncoder.encode(word, "UTF-8");
        from = StringUtils.isEmpty(from) ? "auto" : from;
        String url = "https://translate.google.cn/translate_a/single?client=t";
        url += "&sl=" + from;
        url += "&tl=" + to;
        url += "&hl=zh-CN&dt=at&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&dt=t&ie=UTF-8&oe=UTF-8&source=btn&kc=0";
        url += "&tk=" + token;
        url += "&q=" + word;
        return getResponse(url);
//        Gson gson = new Gson();
//        TranslateResponse translateResponse = gson.fromJson(jsonValue, TranslateResponse.class);
//        return translateResponse.content();
    }

    public String translateWords(List<String> wordList, String from, String to) throws IOException, ScriptException, NoSuchMethodException {
        if (wordList.isEmpty()) {
            return null;
        }
        String ttk = getTKK();
        if (StringUtils.isEmpty(ttk)) {
            return null;
        }
        String key = String.join("", wordList);
        String token = getToken(key, ttk);
        from = StringUtils.isEmpty(from) ? "auto" : from;
        String url = "https://translate.google.cn/translate_a/t?anno=3&client=webapp";
        List<String> data = new ArrayList<>();
        for (String string : wordList) {
            data.add(URLEncoder.encode(string, "UTF-8"));
        }
        String query = String.join("&", data);
        url += "&format=text";
        url += "&v=1.0";
        url += "&key=null";
        url += "&logld=vTE_20190506_00";
        url += "&sl=" + from;
        url += "&tl="+ to;
        url += "&hl=zh-CN";
        url += "&sp=nmt";
        url += "&tc=2";
        url += "&sr=1";
        url += "&tk=" + token;
        url += "&mode=1";
        OkHttpClient client = new OkHttpClient.Builder().build();
        RequestBody requestBody = new FormBody.Builder()
                .add("q", query)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .headers(browserHeaders())
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        assert response.body() != null;
        String result = response.body().string();
        System.out.println(result);
        return result;
    }
}
