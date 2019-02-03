import org.json.simple.JSONObject;

public class Triglyceride extends Data {

    public Triglyceride(JSONObject resource) {
        super(resource);
        Patient patient = Patient.patients.get(getId());
            patient.addTriglyceride(this);
    }
}
