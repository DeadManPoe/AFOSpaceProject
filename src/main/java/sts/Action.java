package sts;

/**
 * Created by giorgiopea on 08/03/17.
 *
 */
public class Action {
    public String type;
    public Object payload;

    public Action(String type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

    @Override
    public String toString() {

        if(this.payload instanceof String){
            return "Action{" +
                    "type='" + type + "\'" +
                    ", payload='" + payload +
                    "'}";
        }
        return "Action{" +
                "type='" + type + "\'" +
                ", payload=" + payload +
                "}";

    }
}
