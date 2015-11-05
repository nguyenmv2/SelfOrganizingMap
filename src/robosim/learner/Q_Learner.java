package robosim.learner;

import robosim.core.Action;
import robosim.core.Simulator;

import java.util.*;

/**
 * Created by my on 11/3/15.
 */
public class Q_Learner {
    Simulator robot;
    State currentState;
    HashMap<State, HashMap<Action, Double>> QTable;
    double learning_rate, discount_rate, explo_prob;
    Reward rewarder;
    Random random;

    public Q_Learner(State currentState, double learning_rate, double discount_rate, double explo_prob, Reward rewarder, Simulator robot){
        this.currentState = currentState;
        this.learning_rate = discount_rate;
        this.explo_prob = explo_prob;
        this.rewarder = rewarder;
        this.random = new Random();
        this.robot = robot;
        initQTable();
    }

    public void initQTable(){
        QTable = new HashMap<State, HashMap<Action, Double>>();
        Arrays.asList(State.values()).forEach(state -> {
            HashMap<Action, Double> actionValMap = new HashMap<>();
            Arrays.asList(Action.values()).forEach(action -> actionValMap.put(action, 0.0));
            QTable.put(state, actionValMap);
        });
    }

    public Action getBestAction(State state){
        if (Math.random() < explo_prob){
            return Action.values()[random.nextInt(Action.values().length)];
        }
        Action preferred = Action.FORWARD;
        double bestVal = Double.MIN_VALUE;
        HashMap<Action, Double> actionMap = QTable.get(state);
        for (Action action : actionMap.keySet()) {
            double val = actionMap.get(action);
            if (val > bestVal) {
                bestVal = val;
                preferred = action;
            }
        }
        return preferred;
    }


    public double getValueForState(State state, Action action){
        HashMap<Action, Double> actionValMap = QTable.getOrDefault(state, new HashMap<>());
        return actionValMap.getOrDefault(action, 0.0);
    }

    public State getNextState(Action action){
        action.applyTo(this.robot);
        State nextState = State.getState(rewarder.getReward(robot));
        return nextState;
    }
    public void updateQTable(State state, Action action, double actionReward) {
        State nextState = getNextState(action);
        double oldVal = getValueForState(state, action);
        double nextStateValue = getValueForState(nextState, action);
        double newVal = (1-learning_rate) * oldVal
                            + learning_rate * (actionReward + discount_rate * nextStateValue);
        QTable.get(state).replace(action, newVal);
        currentState = state;
    }





}
