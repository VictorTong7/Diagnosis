import org.json.simple.JSONObject;

public class HDL extends Data {

    public HDL(JSONObject resource) {
        super(resource);
        Patient patient = Patient.patients.get(getId());
            patient.addHDL(this);
    }
}
