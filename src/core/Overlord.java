package core;

import java.io.File;

import logging.Log;
import map.MapLoader;
import map.Map;
import rrt.PathPlanning;
import state_machine.StateMachine;
import control.Control;

public class Overlord extends Thread {

	StateEstimator se;
	StateMachine sm;
	PathPlanning pp;
	Control c;

	Log l;
	private static long startTime;
	private Map map;

	public Overlord() {
		map = Map.getInstance();
		MapLoader.load(map, new File("gameMaps/practice_field.txt"));

		try {Thread.sleep(50);} catch (InterruptedException e) {}
		
		se = StateEstimator.getInstance();
		sm = StateMachine.getInstance();
		pp = PathPlanning.getInstance();
		c = Control.getInstance();
		
		try {Thread.sleep(50);} catch (InterruptedException e) {}
		
		l = Log.getInstance();
	}
 
	public void start() {
		startTime = System.currentTimeMillis();
		pp.start();
		while (timeRemaining() > 0) {
			
			se.step();
			sm.step();
			c.step();
			l.updatePose();
			
			try {Thread.sleep(50-(System.currentTimeMillis()-startTime));} catch (Exception e){};
		}
		pp.end();
	}
	
	public static long timeRemaining() {
		return (3 * 60 * 1000 - (System.currentTimeMillis() - startTime));
	}

	public static void main(String[] args) {
		System.out.println("Main");

		Overlord me = new Overlord();
		System.out.println("About to start");
		me.start();
	}
}
