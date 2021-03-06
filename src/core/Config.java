package core;

import java.awt.Color;

public class Config {
	// MAP CONFIG
	
    // ROBOT CONFIG
    public static final double WHEELBASE = 0.213995;
    public static final double WHEEL_RADIUS = 0.0492125;
    public static final double WHEEL_CIRCUMFERENCE = WHEEL_RADIUS * Math.PI * 2.0;
    public static final double MAX_VELOCITY = WHEEL_CIRCUMFERENCE * 5.833;
    	
    public static double[][] botPoly = new double[][] {
    	{ -0.15, -0.1 },
    	{ 0.05, -0.1 },
    	{ 0.05, 0.1 },
    	{ -0.15, 0.1 }
    };
	
	// RRT AND PLANNING CONFIG
    public static final int CSPACE_RADIUS_SEGMENTS = 4;
    public static final double BUFFER_SIZE = .00;
    
	public static final double RRT_GOAL_BIAS = .05;
	public static final double MAXLENGTH = 1.5;
	
	
	public static final double minDist = 0;

    public enum BlockColor {
        RED, GREEN, BLUE, YELLOW, NONE
    }

    public static final double blockSize = 0.05; //side of the block
    
    public static BlockColor ColorToBlockColor(Color c) {
    	if (c.equals(Color.RED)){
    		return BlockColor.RED;
    	}
    	else if (c.equals(Color.GREEN)){
    		return BlockColor.GREEN;
    	}
    	else if (c.equals(Color.BLUE)){
    		return BlockColor.BLUE;
    	}
    	else if (c.equals(Color.YELLOW)){
    		return BlockColor.YELLOW;
    	}
    	return BlockColor.NONE;
    }
    
    public static Color BlockColorToColor(BlockColor bc) {
    	if (bc.equals(BlockColor.RED)){
    		return Color.RED;
    	}
    	else if (bc.equals(BlockColor.GREEN)){
    		return Color.GREEN;
    	}
    	else if (bc.equals(BlockColor.BLUE)){
    		return Color.BLUE;
    	}
    	else if (bc.equals(BlockColor.YELLOW)){
    		return Color.YELLOW;
    	}
    	return Color.BLACK;
    }
    

	public static final long CHALLENGE_TIME = 600000;

    public static final int MOTOR_LEFT_DIR_PIN = 8;
	public static final int MOTOR_LEFT_PWM_PIN = 7;
    public static final int MOTOR_RIGHT_DIR_PIN = 10;
	public static final int MOTOR_RIGHT_PWM_PIN = 9;
	public static final int SERVO_GRIP_PIN = 12;
	public static final int SERVO_ELEVATION_PIN = 11;
	public static final int BALL_LAUNCHER_PIN = 37;
	public static final int RANGE_SENSOR_PIN = 36;
	public static final int ENCODER_LEFT_PIN_A = 29;
	public static final int ENCODER_LEFT_PIN_B = 30;
	public static final int ENCODER_RIGHT_PIN_A = 31;
	public static final int ENCODER_RIGHT_PIN_B = 32;
}
