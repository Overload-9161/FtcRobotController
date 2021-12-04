package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.util.hardware.util.Hardware;

@Autonomous(name = "INIT", group = "Test")
//@Disabled
public class INIT extends LinearOpMode {
	
	Hardware r = new Hardware();
	
	@Override
	public void runOpMode() throws InterruptedException {
		r.initRobot(this);
		r.initAuto();
		
		waitForStart();
		
		r.zeroRobot();
		
	}
}