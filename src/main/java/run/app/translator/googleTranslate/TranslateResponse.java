package run.app.translator.googleTranslate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;

@Data
@ToString
@EqualsAndHashCode
class TranslateResponse {
    private ArrayList<TranslateResponseArray> value1;
    private String value2;
    private String value3;
    private String value4;
    private String value5;

    String content() {
        StringBuilder result = new StringBuilder();
        for (TranslateResponseArray array : value1) {
            result.append(array.getList().get(0));
        }
        return result.toString();
    }
}