import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Pressure extends Data {

    private String systolic;

    private String diatolic;

    public Pressure(JSONObject resource) {
        super(resource);
        JSONArray component = (JSONArray) resource.get("component");
        JSONObject first = (JSONObject) component.get(0);
        JSONObject second = (JSONObject) component.get(1);
        JSONObject firstCode = (JSONObject) first.get("code");
        String text = (String) firstCode.get("text");
        if (!text.equals("systolic")) {
            JSONObject temp = first;
            first = second;
            second = temp;
        }
        JSONObject systolic = (JSONObject) first.get("valueQuantity");
        JSONObject diatolic = (JSONObject) second.get("valueQuantity");
        this.systolic = (String) systolic.get("value");
        this.diatolic = (String) diatolic.get("value");
        Patient patient = Patient.patients.get(getId());
        patient.addPressure(this);
    }

    public String getSystolicValue() {
        return this.systolic;
    }

    public String getDiatolicValue() {
        return this.diatolic;
    }
}
