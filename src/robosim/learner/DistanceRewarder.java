package robosim.learner;

import robosim.core.Simulator;

/**
 * Created by my on 11/5/15.
 */
public class DistanceRewarder implements Reward{
    @Override
    public double getReward(Simulator robot) {
        return robot.findClosest();
    }
}
