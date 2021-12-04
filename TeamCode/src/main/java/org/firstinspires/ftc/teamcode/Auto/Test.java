package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.util.Threads.position.IMU.IMUPos;
import org.firstinspires.ftc.teamcode.util.hardware.util.Hardware;

@Autonomous(name = "Test", group = "Dev")
//@Disabled
public class Test extends LinearOpMode {
	
	Hardware r = new Hardware();
	
	IMUPos imuPos;
	
	@Override
	public void runOpMode() throws InterruptedException {
		r.initRobot(this);
		r.initAuto();
		
		imuPos = new IMUPos(this);
		
		BNO055IMU imu;
		imu = hardwareMap.get(BNO055IMU.class, "imu");
		
		waitForStart();
		
		while(opModeIsActive()){
			idle();
		}
		
		imuPos.interrupt();
		
	}
}