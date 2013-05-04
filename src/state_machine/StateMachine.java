package state_machine;

import map.Point;

public class StateMachine {

    private static StateMachine instance;
    
    public State state;
    public Point goal;
    
    public StateMachine() {
        state = new ExploreState(this);
    }
    
    public static StateMachine getInstance() {
        if (instance == null)
            instance = new StateMachine();
        return instance;   
    } 
    
    public void step() {
        state = state.step();
    }
    
    protected void setGoal(Point p) {
        this.goal = p;
    }
    
    public Point getGoal() {
        return goal;
    }
}
