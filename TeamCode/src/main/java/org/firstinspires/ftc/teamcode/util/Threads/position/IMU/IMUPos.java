package org.firstinspires.ftc.teamcode.util.Threads.position.IMU;

import android.graphics.drawable.GradientDrawable;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.bosch.NaiveAccelerationIntegrator;
import com.qualcomm.hardware.bosch.NaiveAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.teamcode.util.Threads.File.FileManager;

public class IMUPos extends Thread {
	
	OpMode opMode;
	FileManager fileManager = null;
	
	int[] startPosition;
	
	ElapsedTime time;
	
	BNO055IMU imu;
	
	GradientDrawable.Orientation angles;
	Acceleration gravity;
	
	/**
	 * Use this to init the IMU position class
	 * @param opMode Just type "this"
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
		parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
		
		imu = opMode.hardwareMap.get(BNO055IMU.class, "imu");
		imu.initialize(parameters);
		
		Position x = new Position();
		x.x = 0;
		x.y = 0;
		Velocity v = new Velocity();
		v.xVeloc = 0;
		v.yVeloc = 0;
		v.zVeloc = 0;
		
		imu.startAccelerationIntegration(x, v, 1000);
		
		super.start();
	}
	
	@Override
	public void run() {
		while(!this.isInterrupted()){
			// TODO: Read the IMU data and use a PID loop to get location
			opMode.telemetry.addLine("IMU |")
					.addData("X", imu.getPosition().x)
					.addData("Y", imu.getPosition().y);
			opMode.telemetry.update();
		}
		super.run();
	}
	
	@Override
	public void interrupt() {
		// TODO: Add a wait and/or make the interrupt the .stop()
		super.interrupt();
	}
}