package sts;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by giorgiopea on 08/03/17.
 *
 */
public class ActionFactory {

    private List<String> actionTypes;

    public ActionFactory(List<String> actionTypes) {
        this.actionTypes = actionTypes;
    }

    public Action getAction(String type, Object payload){
        if(actionTypes.contains(type)){
            return new Action(type,payload);
        }
        else{
            throw new NoSuchElementException("The given action type does not match any registered action type");
        }
    }
}
