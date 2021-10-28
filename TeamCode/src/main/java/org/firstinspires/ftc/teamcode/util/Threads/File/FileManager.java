package org.firstinspires.ftc.teamcode.util.Threads.File;

import android.os.Environment;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class FileManager extends Thread {
	File directory, file;
	FileOutputStream fos;
	
	public DcMotor[]   motor   = null;
	public DcMotorEx[] motorEx = null;
	
	OpMode op;
	
	List<String> buffer = new ArrayList<>();
	
	Timer timer = new Timer();
	
	TimerTask tt;
	
	/**
	 * Init the file manager
	 * @param Type Weather this is for Autonomous or TeleOp
	 */
	public FileManager(String Type, OpMode op){
		this.op = op;
		init(Type);
	}
	
	/**
	 * Init FileManager
	 * @param Type Weather its TeleOp or Autonomous
	 */
	private void init(String Type){
		try {
			directory = new File(Environment.getExternalStorageDirectory()+Type+"/");
			if(!directory.exists())
				directory.mkdirs();
			directory = new File(Environment.getExternalStorageDirectory()+Type+"/"+Type);
			directory.mkdirs();
			file = new File(directory, "Log.txt");
			file.mkdir();
			fos = new FileOutputStream(file);
			Log.d("File", "Working");
		} catch (IOException e){
			e.printStackTrace();
			Log.wtf("File", "Failed");
		}
	}
	
	ElapsedTime time;
	boolean Auto_Tele = false; // False for Auto, true for Tele
	
	/**
	 * Start the file writer for teleop. This will log all data from the gamepads along with everything else
	 * @param time The timer you want to use
	 */
	public void StartTeleOp(ElapsedTime time){
		this.time = time;
		Auto_Tele = true;
		timer.schedule(runTeleLog(), 0, 50);
		this.start();
	}
	
	/**
	 * Start the file writer for autonomous. This will only log sensor data.
	 * @param time The timer you want to use
	 */
	public void StartAuto(ElapsedTime time){
		this.time = time;
		timer = new Timer();
		this.timer.schedule(new AutoLog(this.time, this), 0, 50);
		this.start();
	}
	
	/**
	 * Point this file writer to the motors on your bot
	 * @param motors The array of all your motors
	 */
	public void initMotors(DcMotor[] motors){
		this.motor = motors;
	}
	
	@Override
	public void run(){
		this.setPriority(8);
		while(!this.isInterrupted()){
			writeToFile();
			try {
				sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Write everything in the buffer to the file, this runs periodically
	 */
	public void writeToFile(){
		for(int i=0;i<buffer.size();i++){
			try {
				fos.write(buffer.get(i).getBytes());
				fos.write("\n".getBytes());
				fos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		buffer.clear();
	}
	
	/**
	 * Log all of the data from the gamepads to the buffer
	 * @param time When you are logging this
	 */
	public void writeGamepad(double time){
		// Gamepad 1
		if(op.gamepad1.getGamepadId() > 0) {
			// Sticks
			writeFile("Gamepad1-LStick?Plain", new float[]{op.gamepad1.left_stick_x, op.gamepad1.left_stick_y}, time);
			writeFile("Gamepad1-RStick?Plain", new float[]{op.gamepad1.right_stick_x, op.gamepad1.right_stick_y}, time);
			// YBAX
			writeFile("Gamepad1-YBAX", new boolean[]{op.gamepad1.y, op.gamepad1.b, op.gamepad1.a, op.gamepad1.x}, time);
			// Triggers
			writeFile("Gamepad1-LT", new float[]{op.gamepad1.left_trigger, op.gamepad1.right_trigger}, time);
			// Bumpers
			writeFile("Gamepad1-B", new boolean[]{op.gamepad1.left_bumper, op.gamepad1.right_bumper}, time);
			// Dpad
			writeFile("Gamepad1-DP", new boolean[]{op.gamepad1.dpad_up, op.gamepad1.dpad_right, op.gamepad1.dpad_down, op.gamepad1.dpad_left},time);
			// Random
			writeFile("Gamepad1-R", new boolean[]{op.gamepad1.start, op.gamepad1.back, op.gamepad1.left_stick_button, op.gamepad1.right_stick_button}, time);
			
		}
		// Gamepad 2
		if(op.gamepad2.getGamepadId() > 0){
			// Sticks
			writeFile("Gamepad2-LStick?Plain", new float[]{op.gamepad2.left_stick_x, op.gamepad2.left_stick_y}, time);
			writeFile("Gamepad2-RStick?Plain", new float[]{op.gamepad2.right_stick_x, op.gamepad2.right_stick_y}, time);
			// YBAX
			writeFile("Gamepad2-YBAX", new boolean[]{op.gamepad2.y, op.gamepad2.b, op.gamepad2.a, op.gamepad2.x}, time);
			// Triggers
			writeFile("Gamepad2-LT", new float[]{op.gamepad2.left_trigger, op.gamepad2.right_trigger}, time);
			// Bumpers
			writeFile("Gamepad2-B", new boolean[]{op.gamepad2.left_bumper, op.gamepad2.right_bumper}, time);
			// Dpad
			writeFile("Gamepad2-DP", new boolean[]{op.gamepad2.dpad_up, op.gamepad2.dpad_right, op.gamepad2.dpad_down, op.gamepad2.dpad_left}, time);
			// Random
			writeFile("Gamepad2-R", new boolean[]{op.gamepad2.start, op.gamepad2.back, op.gamepad2.left_stick_button, op.gamepad2.right_stick_button}, time);
		}
	}
	
	/**
	 * White data from the motors to the buffer
	 * @param time When you are logging this
	 */
	public void writeMotors(double time){
		if(motor != null){
			for(DcMotor dcMotor : motor){
				writeFile(String.valueOf(op.hardwareMap.getNamesOf(dcMotor)), new double[]{dcMotor.getPower(), dcMotor.getTargetPosition(), dcMotor.getCurrentPosition()}, time);
			}
		}
	}
	
	public interface CustomData{
		void run(double time);
	}
	
	public void repeat(CustomData dta){
		Timer t = new Timer();
		t.schedule(runLambda(dta), 0, 50);
	}
	
	// The max amount of char in a Break line
	int maxLength = 100;
	int charLength;
	
	/**
	 * This method is made to add a bar across the file that outlines different things that happen
	 * @param name The label of the bar
	 */
	public void Break(String name){
		String thing = "";
		char[] Name = name.toCharArray();
		charLength = maxLength - Name.length;
		for(int i = 0; i<charLength; i++){
			if(i==((int) charLength/2)){
				thing+=name;
			}
			thing+="=";
		}
		Log.d("Thing", thing);
		buffer.add(thing);
	}
	
	/**
	 * Close the file manager
	 * <p>
	 * You won't be able to save anything else to the file
	 */
	public void close(){
		try {
			Break("End of File");
			timer.cancel();
			writeToFile();
			fos.close();
			this.interrupt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Write to the file buffer
	 * @param Name The name of the things that you are logging
	 * @param LogThing The thing you want to log
	 * @param time When you logged this thing
	 */
	public void writeFile(String Name, Object LogThing, double time){
		buffer.add(Name+"/"+(int)time+":"+LogThing);
	}
	
	private TimerTask runTeleLog(){
		double time = this.time.milliseconds();
		writeGamepad(time);
		writeMotors(time);
		return null;
	}
	
	private TimerTask runAutoLog(){
		double time = this.time.milliseconds();
		writeMotors(time);
		return null;
	}
	
	private TimerTask runLambda(CustomData CD){
		double time = this.time.milliseconds();
		CD.run(time);
		return null;
	}
	
}

class AutoLog extends TimerTask {
	
	FileManager f;
	ElapsedTime time;
	public AutoLog(ElapsedTime time, FileManager f){
		this.time = time;
		this.f = f;
	}
	
	public void run(){
		double t = time.milliseconds();
		f.writeGamepad(t);
		f.writeMotors(t);
	}
}
