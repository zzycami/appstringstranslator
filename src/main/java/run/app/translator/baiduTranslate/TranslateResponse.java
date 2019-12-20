package run.app.translator.baiduTranslate;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@EqualsAndHashCode
public class TranslateResponse {
    private String from;
    private String to;

    @SerializedName("trans_result")
    private List<TranslateResult> transResult;
}
