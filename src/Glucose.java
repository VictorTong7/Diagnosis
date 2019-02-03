import org.json.simple.JSONObject;

public class Glucose extends Data {

    public Glucose(JSONObject resource) {
        super(resource);
        Patient patient = Patient.patients.get(getId());
            patient.addGlucose(this);
    }
}