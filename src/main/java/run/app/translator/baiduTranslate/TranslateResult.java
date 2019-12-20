package run.app.translator.baiduTranslate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class TranslateResult {
    private String src;
    private String dst;
}
