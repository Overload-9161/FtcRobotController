package org.firstinspires.ftc.teamcode.util.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

/**
 * Use this during autonomous to get the directions and distances you want
 */
public class HardAuto extends Hardware{
	
	/**
	 * Set the encoders of the motors to the distance you want to go
	 * @param driveMode The direction you want to move
	 * @param distance The distance you want to move
	 */
	public void mechanumEncDrive(DriveMode driveMode, int distance){
		switch (driveMode){
			case FORWARDS:
				frontLeft.setTargetPosition(distance);
				frontRight.setTargetPosition(distance);
				backLeft.setTargetPosition(distance);
				backRight.setTargetPosition(distance);
				break;
			case BACKWARDS:
				frontLeft.setTargetPosition(-distance);
				frontRight.setTargetPosition(-distance);
				backLeft.setTargetPosition(-distance);
				backRight.setTargetPosition(-distance);
				break;
			case LEFT:
				frontLeft.setTargetPosition(-distance);
				frontRight.setTargetPosition(distance);
				backLeft.setTargetPosition(distance);
				backRight.setTargetPosition(-distance);
				break;
			case RIGHT:
				frontLeft.setTargetPosition(distance);
				frontRight.setTargetPosition(-distance);
				backLeft.setTargetPosition(-distance);
				backRight.setTargetPosition(distance);
				break;
			case FRONT_LEFT:

				break;
			case FRONT_RIGHT:
				
				break;
			case BACK_LEFT:
				// Dont Use
				break;
			case BACK_RIGHT:
				// Dont Use
				break;
			case CLOCKWISE:
				frontLeft.setTargetPosition(distance);
				frontRight.setTargetPosition(-distance);
				backLeft.setTargetPosition(distance);
				backRight.setTargetPosition(-distance);
				break;
			case COUNTERCLOCKWISE:
				frontLeft.setTargetPosition(-distance);
				frontRight.setTargetPosition(distance);
				backLeft.setTargetPosition(-distance);
				backRight.setTargetPosition(distance);
				break;
		}
	}
	
	/**
	 * Move the robot in certain directions
	 * @param driveMode The direction you want to move
	 * @param power The power from 0 to 1.0 of how fast you want to drive at
	 */
	public void mechanumDrive(DriveMode driveMode, double power){
		switch (driveMode){
			case FORWARDS:
				frontLeft.setPower(power);
				frontRight.setPower(power);
				backLeft.setPower(power);
				backRight.setPower(power);
				break;
			case BACKWARDS:
				frontLeft.setPower(-power);
				frontRight.setPower(power);
				backLeft.setPower(-power);
				backRight.setPower(-power);
				break;
			case LEFT:
				frontLeft.setPower(-power);
				frontRight.setPower(power);
				backLeft.setPower(power);
				backRight.setPower(-power);
				break;
			case RIGHT:
				frontLeft.setPower(power);
				frontRight.setPower(-power);
				backLeft.setPower(-power);
				backRight.setPower(power);
				break;
			case FRONT_LEFT:
				//Dont use
				break;
			case FRONT_RIGHT:
				// Dont use
				break;
			case BACK_LEFT:
				// Dont Use
				break;
			case BACK_RIGHT:
				// Dont Use
				break;
			case CLOCKWISE:
				frontLeft.setPower(power);
				frontRight.setPower(-power);
				backLeft.setPower(power);
				backRight.setPower(-power);
				break;
			case COUNTERCLOCKWISE:
				frontLeft.setPower(-power);
				frontRight.setPower(power);
				backLeft.setPower(-power);
				backRight.setPower(power);
				break;
		}
	}
	
	public void setToStill(){
		for(DcMotor dcMotor : drive){
			dcMotor.setPower(0);
		}
	}
	
	public void runDistance(double power, int distance){
	
	}
	
	public void runAngle(double power, int degree){
		double deg = Math.toRadians(degree);
		
		//equations taking the polar coordinates and turning them into motor powers
		double vx = power * Math.cos(deg + (Math.PI / 4)); // determine the velocity in the Y-axis
		double vy = power * Math.sin(deg + (Math.PI / 4)); // determine the velocity in the X-axis
		
		frontLeft.setPower(vx);
		frontRight.setPower(vy);
		backLeft.setPower(vy);
		backRight.setPower(vx);
	}
	
	/**
	 * To set the dive direction
	 */
	public enum DriveMode{
		FORWARDS,
		BACKWARDS,
		LEFT,
		RIGHT,
		FRONT_LEFT,
		FRONT_RIGHT,
		BACK_LEFT,
		BACK_RIGHT,
		CLOCKWISE,
		COUNTERCLOCKWISE
	}
	
}