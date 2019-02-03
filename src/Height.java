import org.json.simple.JSONObject;

public class Height extends Data {

    public Height(JSONObject resource) {
        super(resource);
        Patient patient = Patient.patients.get(getId());
            patient.addHeight(this);
    }

}
