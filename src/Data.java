import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.ArrayList;
import javax.swing.text.Style;

public abstract class Data {

    private String id;
    private String value;
    private String time;

    public static Data newBuilder(JSONObject resource) {
        JSONObject code = (JSONObject) resource.get("code");
        String text = (String) code.get("text");
        switch (text) {
            case "height":
                return new Height(resource);
            case "bmi":
                return new Bmi(resource);
            case "weight":
                return new Weight(resource);
            case "A1C":
                return new A1C(resource);
            case "glucose":
                return new Glucose(resource);
            case "glucose_post_meal":
                return new GlucosePostmeal(resource);
            case "pressure":
                return new Pressure(resource);
            case "pulse_rate":
                return new Pulse(resource);
            case "whiteBloodCount":
                return new Wbc(resource);
            case "hdl":
                return new HDL(resource);
            case "ldl":
                return new LDL(resource);
            case "triglyceride":
                return new Triglyceride(resource);
        }
        return null;
    }

    protected Data(JSONObject resource) {
        JSONObject subject = (JSONObject) resource.get("subject");
        String reference = (String) subject.get("reference");
        this.id = reference.split("/")[1];
        JSONObject valueQuantity = (JSONObject) resource.get("valueQuantity");
        if(valueQuantity != null){
            this.value = (String) valueQuantity.get("value");
        }
        this.time = (String) resource.get("effectiveDateTime");
    }

    public String getValue() {
        return value;
    }

    public String getId() {
        return id;
    }

    public String getTime() {
        return time;
    }
}
