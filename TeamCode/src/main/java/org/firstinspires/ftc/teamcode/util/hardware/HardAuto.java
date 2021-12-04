package org.firstinspires.ftc.teamcode.util.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.util.hardware.util.Hardware;

/**
 * Use this during autonomous to get the directions and distances you want
 */
@SuppressWarnings(value = "unused")
public class HardAuto extends Hardware{
	
	OpMode opMode;
	
	BNO055IMU imu;
	Orientation angles;
	
	@Override
	public void initRobot(OpMode opMode) {
		this.opMode = opMode;
		super.initRobot(opMode);
	}
	
	@Override
	public void initAuto() {
		
		BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
		parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
		parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
		parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
		parameters.loggingEnabled      = true;
		parameters.loggingTag          = "IMU";
		parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
		
		imu = opMode.hardwareMap.get(BNO055IMU.class, "imu");
		imu.initialize(parameters);
		
		super.initAuto();
	}
	
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
			case FRONT_RIGHT:
				// Don't use
				break;
			case BACK_LEFT:
			case BACK_RIGHT:
				// Don't Use
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
				double v1 = power * Math.cos(Math.toRadians(45) + (Math.PI / 4));
				double v2 = power * Math.sin(Math.toRadians(45) + (Math.PI / 4));
				frontLeft.setPower(v1);
				frontRight.setPower(v2);
				backLeft.setPower(v2);
				backRight.setPower(v1);
				break;
			case FRONT_RIGHT:
				// Don't use
				break;
			case BACK_LEFT:
			case BACK_RIGHT:
				// Don't Use
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
	
	/**
	 * Rotate te robot to a specific degree
	 * @param power The power of the wheels
	 * @param angle the angle you want to go to
	 */
	public void rotate(double power, int angle){
		while(angles.firstAngle > angle+0.1 && angles.firstAngle < angle-0.1){
			angles  = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
			if(angles.firstAngle < angle){
				frontLeft.setPower(power);
				backLeft.setPower(power);
				frontRight.setPower(-power);
				backRight.setPower(-power);
			}else if(angles.firstAngle > angle){
				frontLeft.setPower(-power);
				backLeft.setPower(-power);
				frontRight.setPower(power);
				backRight.setPower(power);
			}
		}
	}
	
	/**
	 * Set all drive motors to a power of 0
	 */
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
	
	/*==============================================
	 * This is development past this point
	 *==============================================*/
	
	/**
	 * This method is to
	 * @param distance The distance you want to move
	 * @param deviation The distance of the deviation
	 * @param speed The double speed you want to run at
	 */
	public void spline(int distance, int deviation, double speed){
	
	}
	
}