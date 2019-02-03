import org.json.simple.JSONObject;

public class GlucosePostmeal extends Data {

    public GlucosePostmeal(JSONObject resource) {
        super(resource);
        Patient patient = Patient.patients.get(getId());
            patient.addGlucosePostmeal(this);
    }
}
