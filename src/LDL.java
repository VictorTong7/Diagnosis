import org.json.simple.JSONObject;

public class LDL extends Data {

    public LDL(JSONObject resource) {
        super(resource);
        Patient patient = Patient.patients.get(getId());
            patient.addLDL(this);
    }
}