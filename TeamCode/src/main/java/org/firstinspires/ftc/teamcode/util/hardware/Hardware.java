package org.firstinspires.ftc.teamcode.util.hardware;

import android.app.Application;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class Hardware extends Application {
	
	HardwareMap hwMap;
	Telemetry   telemetry;
	OpMode      opMode;
	
	private final ElapsedTime Timer = new ElapsedTime();
	public ElapsedTime time = new ElapsedTime();
	
	public DcMotor frontLeft;
	public DcMotor frontRight;
	public DcMotor backLeft;
	public DcMotor backRight;
	
	/** This is all of our drive motors in an array for ease of use */
	public DcMotor[] drive;
	/** This is all of our motors in an array for ease of use */
	public DcMotor[] allMotors;
	
	public AnalogInput potentiometer;
	
	Servo[] servo;
	
	JSONObject jsonObject, motors, driveMotors;
	
	/**
	 * Use this to initiate the robot
	 * @param opMode Just use "this"
	 */
	public void initRobot(OpMode opMode){
		this.opMode = opMode;
		this.hwMap = opMode.hardwareMap;
		this.telemetry = opMode.telemetry;
		try {
			// Change "Robot.json" to the file that you are using
			jsonObject = new JSONObject(Objects.requireNonNull(loadJSONFromAsset("Robot.json")));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		initHardware();
	}
	
	/**
	 * Use this to initiate the robot
	 * @param opMode Just use "this"
	 * @param JSON The name of you JSON file in your
	 */
	public void initRobot(OpMode opMode, String JSON){
		this.opMode = opMode;
		this.hwMap = opMode.hardwareMap;
		this.telemetry = opMode.telemetry;
		try {
			// Change "Robot.json" to the file that you are using
			jsonObject = new JSONObject(Objects.requireNonNull(loadJSONFromAsset(JSON)));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		initHardware();
	}
	
	void initHardware() {
		Log.d("Hardware-init", "Hardware init starting");
		try {
			// Do not edit this
			motors = jsonObject.getJSONObject("Hardware").getJSONObject("DcMotors");
			
			// Do not edit this
			driveMotors = motors.getJSONObject("Drive");
			frontLeft   = hwMap.get(DcMotor.class, String.valueOf(driveMotors.getJSONObject("Front Left Motor").get("name")));
			frontRight  = hwMap.get(DcMotor.class, String.valueOf(driveMotors.getJSONObject("Front Right Motor").get("name")));
			backLeft    = hwMap.get(DcMotor.class, String.valueOf(driveMotors.getJSONObject("Back Left Motor").get("name")));
			backRight   = hwMap.get(DcMotor.class, String.valueOf(driveMotors.getJSONObject("Back Right Motor").get("name")));
			drive 		= new DcMotor[]{frontLeft, frontRight, backLeft, backRight};
			
			if (String.valueOf(driveMotors.getJSONObject("Front Left Motor").get("BreakType")).equals("float")) {
				frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
			} else {
				frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
			}
			
			if (String.valueOf(driveMotors.getJSONObject("Front Right Motor").get("BreakType")).equals("float")) {
				frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
			} else {
				frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
			}
			
			if (String.valueOf(driveMotors.getJSONObject("Back Left Motor").get("BreakType")).equals("float")) {
				backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
			} else {
				backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
			}
			
			if (String.valueOf(driveMotors.getJSONObject("Back Right Motor").get("BreakType")).equals("float")) {
				backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
			} else {
				backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
			}
			
			if ((Boolean) driveMotors.getJSONObject("Front Left Motor").get("Reverse")) 	frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
			if ((Boolean) driveMotors.getJSONObject("Front Right Motor").get("Reverse")) 	frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
			if ((Boolean) driveMotors.getJSONObject("Back Left Motor").get("Reverse")) 		backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
			if ((Boolean) driveMotors.getJSONObject("Back Right Motor").get("Reverse")) 	backRight.setDirection(DcMotorSimple.Direction.REVERSE);
			
			potentiometer = hwMap.get(AnalogInput.class, String.valueOf(jsonObject.getJSONObject("Sensors").getJSONObject("potentiometer").get("name")));
			
			// Add your own
			
			
			
			allMotors = new DcMotor[]{frontLeft, frontRight, backLeft, backRight};
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Use this method to init the robot for autonomous
	 */
	public void initAuto(){
		setDriveMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//		for(Servo servo : servo) servo.setPosition(0);
		waiter(500);
	}
	
	/**
	 * Set the motor mode for the drive motors
	 * @param mode {@link DcMotor.RunMode}
	 */
	public void setDriveMotorMode(DcMotor.RunMode mode){
		for(DcMotor dcMotor : drive) dcMotor.setMode(mode);
	}
	
	/**
	 * Use this method to home everything on the robot
	 */
	public void zeroRobot(){
		// TODO: Make this method
		// HELP: Check what angles are what voltages
	}
	
	/*=====================================================
	  || Constants, DO NOT EDIT please!
	  =====================================================*/
	
	private String loadJSONFromAsset(String fileName) {
		String json = null;
		try {
//            InputStream is = getApplicationContext().getAssets().open("Field.json");
			
			InputStream is = hwMap.appContext.getAssets().open(fileName);
			
			int size = is.available();
			
			byte[] buffer = new byte[size];
			
			is.read(buffer);
			
			is.close();
			
			json = new String(buffer, "UTF-8");
			
			
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;
//		Log.d("Test", json + "\n Test");
//		Log.d("Test", "Running");
	}
	
	/**
	 * Wait for a specified amount of time
	 * @param time in milliseconds
	 */
	public void waiter(int time) {
		Timer.reset();
		while (true) if (!(Timer.milliseconds() < time)) break;
	}
	
}