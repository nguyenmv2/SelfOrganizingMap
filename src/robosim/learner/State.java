package robosim.learner;

/**
 * Created by my on 11/3/15.
 */
public enum State {
    CLEAR,  //Nothing appears on sonar( range = 30 ), forward we march
    SAFE,   //Something is in the sonar, might have to avoid
    COLLIDING; //Collision is bad

    public static State getState(double value){
        if (30 <= value) {
            return CLEAR;
        } else if ( 30 > value && value > 1){
            return SAFE;
        } else {
            return COLLIDING;
        }
    }

}
