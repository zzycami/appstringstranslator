package run.app.translator.googleTranslate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;

@Data
@ToString
@EqualsAndHashCode
class TranslateResponseArray {
    private ArrayList<String> list;
}
