package robosim.ai;

import robosim.core.Action;
import robosim.core.Simulator;
import robosim.learner.DistanceRewarder;
import robosim.learner.Q_Learner;
import robosim.learner.Reward;
import robosim.learner.State;

import java.util.HashMap;

/**
 * Created by my on 11/3/15.
 */
public class BasicQLearner implements Controller{
    Q_Learner learner;
    Reward rewarder;
    double learning_rate = 0.5, discount = 0.2, explo_prob = 0.1;
    private State previousState;
    private Action previousAction;

    @Override
    public void control(Simulator sim) {

        if (rewarder == null){
            rewarder = new DistanceRewarder();
        }
        State currentState = getState(sim);
        if (learner == null){
            learner = new Q_Learner(currentState, learning_rate, discount, explo_prob, rewarder,sim);
        }
        if (previousState == null){
            previousState = currentState;
        }
        Action currentAction = learner.getBestAction(currentState);
        currentAction.applyTo(sim);
        if (sim.wasHit()){
            learner.updateQTable(currentState, currentAction, -learning_rate);
            learner.updateQTable(previousState, previousAction, -learning_rate);
        }
        else if(previousAction == Action.FORWARD){
            learner.updateQTable(previousState, previousAction, learning_rate);
        }
        else if (previousAction != null){
            learner.updateQTable(previousState, previousAction, learning_rate*learning_rate);
        }
        previousState = currentState;
        previousAction = currentAction;
    }

    public State getState(Simulator sim){
        double val = rewarder.getReward(sim);
        System.out.println(val);
        return State.getState(val);
    }

}
