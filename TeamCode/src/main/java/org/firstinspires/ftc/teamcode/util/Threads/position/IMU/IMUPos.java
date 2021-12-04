package org.firstinspires.ftc.teamcode.util.Threads.position.IMU;

import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.teamcode.util.IMUUtil.Accel;
import org.firstinspires.ftc.teamcode.util.Threads.File.FileManager;

@SuppressWarnings(value = "unused")
public class IMUPos extends Thread {
	
	OpMode opMode;
	HardwareMap hwMap;
	FileManager fileManager = null;
	
	/**
	 * Use this to get the location of the robot<br><br>
	 * DO NOT WRITE TO THIS!!!
	 */
	public int[] startPosition = {0,0};
	
	public Position position = new Position();
	public Velocity velocity = new Velocity();
	
	ElapsedTime time;
	
	BNO055IMU imu;
	
	Orientation angles;
	Acceleration gravity;
	
	/**
	 * Use this when you do not have your position or don't want to pass it
	 * @param opMode Type "this"
	 */
	public IMUPos(OpMode opMode){
		this.opMode = opMode;
		this.hwMap = opMode.hardwareMap;
		this.start();
	}
	
	/**
	 * Use this to init the IMU position class
	 * @param opMode Type "this"
	 * @param startPosition new int[]{x,y}
	 * @param time Elapsed time
	 */
	public IMUPos(OpMode opMode, int[] startPosition, ElapsedTime time){
		this.opMode = opMode;
		this.startPosition = startPosition;
		this.time = time;
		this.start();
	}
	
	/**
	 * Init the file manager to log your data
	 * @param fileManager {@link org.firstinspires.ftc.teamcode.util.Threads.File.FileManager}
	 */
	public void initFile(FileManager fileManager){
		this.fileManager = fileManager;
	}
	
	@Override
	public synchronized void start() {
		// TODO: Init the IMU
		BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
		parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
		parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
		parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
		parameters.loggingEnabled      = true;
		parameters.loggingTag          = "IMU";
		parameters.accelerationIntegrationAlgorithm = new Accel();
		
		imu = hwMap.get(BNO055IMU.class, "imu");
		imu.initialize(parameters);
		
		position.x = 0;
		position.y = 0;
		
		velocity.xVeloc = 0;
		velocity.yVeloc = 0;
		
		// Start the logging of measured acceleration
		imu.startAccelerationIntegration(position, velocity, 50);
		
		Log.i("IMU", "IT HAS INITED!");
		super.start();
	}
	
	@Override
	public void run() {
		
		opMode.telemetry.addAction(() -> {
			angles  = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
			gravity = imu.getGravity();
		});
		opMode.telemetry.addLine("Position thingy")
				.addData("X", () -> String.valueOf(position.x))
				.addData("Y", () -> String.valueOf(position.y));
		// Position Data
		opMode.telemetry.addLine()
				.addData("Pos X", () -> String.valueOf(imu.getPosition().x))
				.addData("Pos Y", () -> String.valueOf(imu.getPosition().y))
				.addData("Pos Z", () -> String.valueOf(imu.getPosition().z));
		// General Acceleration
		opMode.telemetry.addLine()
				.addData("GA X","%2.2f", () -> imu.getAcceleration().xAccel)
				.addData("GA Y","%2.2f", () -> imu.getAcceleration().yAccel)
				.addData("GA Z","%2.2f", () -> imu.getAcceleration().zAccel);
		// Linear Acceleration
		opMode.telemetry.addLine()
				.addData("Lin X","%2.2f", () -> imu.getLinearAcceleration().xAccel)
				.addData("Lin Y","%2.2f", () -> imu.getLinearAcceleration().yAccel)
				.addData("Lin Z","%2.2f", () -> imu.getLinearAcceleration().zAccel);
		// Angles
		opMode.telemetry.addLine()
				.addData("First","%2.2f",  () -> angles.firstAngle)
				.addData("Second","%2.2f", () -> angles.secondAngle)
				.addData("Third","%2.2f",  () -> angles.thirdAngle);
		while(!this.isInterrupted()){
			// TODO: Read the IMU data and use a PID loop to get location
			opMode.telemetry.update();
		}
	}
	
	/**
	 * Get the data from the IMU
	 */
	public static class getData{
		/**
		 * The XYZ data
		 */
		public static double x;
		public static double y;
		public static double z;
		public static double yaw, pitch, roll;
	}
	
	/**
	 * If you want direct access to the IMU
	 * @return The imu class that this class initiated
	 */
	public BNO055IMU getIMU(){
		return imu;
	}
	
	@Override
	public void interrupt() {
		// TODO: Add a wait and/or make the interrupt the .stop()
		super.interrupt();
	}
	/*
	opMode.telemetry.addLine("IMU Position |")
			.addData("X", imu.getPosition().x)
			.addData("Y", imu.getPosition().y);
	opMode.telemetry.addLine("IMU Accelerations |")
			.addData("X", imu.getAcceleration().xAccel)
			.addData("Y", imu.getAcceleration().yAccel)
			.addData("Z", imu.getAcceleration().zAccel);
	opMode.telemetry.addLine("IMU Velocity |")
			.addData("X", imu.getVelocity().xVeloc)
			.addData("Y", imu.getVelocity().yVeloc)
			.addData("Z", imu.getVelocity().zVeloc);
	opMode.telemetry.addLine("IMU Angular |")
			.addData("X", imu.getAngularVelocity().xRotationRate)
			.addData("Y", imu.getAngularVelocity().yRotationRate)
			.addData("Z", imu.getAngularVelocity().zRotationRate);
	opMode.telemetry.addLine("IMU Rotation |")
			.addData("First",  imu.getAngularOrientation().firstAngle)
			.addData("Second", imu.getAngularOrientation().secondAngle)
			.addData("Third",  imu.getAngularOrientation().thirdAngle);
	opMode.telemetry.addLine("Linear Acceleration |")
			.addData("X", imu.getLinearAcceleration().xAccel)
			.addData("Y", imu.getLinearAcceleration().yAccel)
			.addData("Z", imu.getLinearAcceleration().zAccel);
	opMode.telemetry.update();
	 */
}