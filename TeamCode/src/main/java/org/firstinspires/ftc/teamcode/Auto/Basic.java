package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.Threads.File.FileManager;
import org.firstinspires.ftc.teamcode.util.Threads.position.IMU.IMUPos;
import org.firstinspires.ftc.teamcode.util.hardware.AutoTransitioner;
import org.firstinspires.ftc.teamcode.util.hardware.HardAuto;
import org.firstinspires.ftc.teamcode.util.hardware.Hardware;

@Autonomous(name = "Basic", group = "Dev")
//@Disabled
public class Basic extends LinearOpMode {
	
	Hardware r = new Hardware();
	HardAuto auto = new HardAuto();
	FileManager fileManager;
	ElapsedTime time = new ElapsedTime();
	
	//IMUPos imuPos = new IMUPos(this, new int[] {0,0}, time);
	
	@Override
	public void runOpMode() throws InterruptedException {
		AutoTransitioner.transitionOnStop(this, "TeleOp_Basic");
		r.initRobot(this);
		r.initAuto();
		
		fileManager = new FileManager("Auto", this);
		fileManager.StartAuto(time);
		fileManager.initMotors(r.allMotors);
		
		waitForStart();
		time.startTime();
		
		auto.mechanumDrive(HardAuto.DriveMode.FORWARDS, 0.6);
		r.waiter(1000);
		auto.setToStill();
		
	}
	
	
}