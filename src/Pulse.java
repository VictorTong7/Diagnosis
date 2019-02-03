import org.json.simple.JSONObject;

public class Pulse extends Data {

    public Pulse(JSONObject resource) {
        super(resource);
        Patient patient = Patient.patients.get(getId());
            patient.addPulse(this);
    }
}
