package robosim.learner;

import robosim.core.Simulator;

/**
 * Created by my on 11/4/15.
 */
public interface Reward {
    public double getReward(Simulator robot);
}
