package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.Threads.File.FileManager;
import org.firstinspires.ftc.teamcode.util.Threads.position.IMU.IMUPos;
import org.firstinspires.ftc.teamcode.util.hardware.AutoTransitioner;
import org.firstinspires.ftc.teamcode.util.hardware.Hardware;

@Autonomous(name = "Basic", group = "Test")
//@Disabled
public class Basic extends LinearOpMode {
	
	Hardware r = new Hardware();
	FileManager fileManager = new FileManager("Auto", this);
	ElapsedTime time = new ElapsedTime();
	
	IMUPos imuPos = new IMUPos(this, new int[] {0,0}, time);
	
	@Override
	public void runOpMode() throws InterruptedException {
		AutoTransitioner.transitionOnStop(this, "TeleOp_Basic");
		r.initRobot(this);
		r.initAuto();
		
		fileManager.StartAuto(time);
		fileManager.initExMotors(r.allMotors);
		
		waitForStart();
		time.startTime();
	}
}