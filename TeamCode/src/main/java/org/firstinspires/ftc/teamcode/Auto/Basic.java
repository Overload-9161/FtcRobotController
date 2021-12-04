package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.Threads.File.FileManager;
import org.firstinspires.ftc.teamcode.util.Threads.cam.Camera;
import org.firstinspires.ftc.teamcode.util.hardware.AutoTransitioner;
import org.firstinspires.ftc.teamcode.util.hardware.HardAuto;

@Autonomous(name = "Basic", group = "Dev")
//@Disabled
public class Basic extends LinearOpMode {
	
	HardAuto r = new HardAuto();
	FileManager fileManager;
	ElapsedTime time = new ElapsedTime();
	Camera camera;
	
	//IMUPos imuPos = new IMUPos(this, new int[] {0,0}, time);
	
	@Override
	public void runOpMode() throws InterruptedException {
		{
			AutoTransitioner.transitionOnStop(this, "TeleOp_Basic");
			r.initRobot(this);
			r.initAuto();
			
			camera = new Camera(this);
			camera.openCamera();
			camera.startCamera();
			
			fileManager = new FileManager(this);
			fileManager.init("Auto");
			fileManager.initMotors(r.allMotors);
			fileManager.StartAuto(time);
			
			waitForStart();
			time.startTime();
			
			camera.takePhoto();
			
			r.waiter(1000);
			
			camera.stopCamera();
			
			r.setDriveMotorMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		}
		/*==========================================
					Edit from here down!
		  ==========================================*/
//		r.mechanumDrive(HardAuto.DriveMode.FORWARDS, 0.6);
//		r.frontLeft.setPower(1);
//		r.frontRight.setPower(1);
//		r.backLeft.setPower(1);
//		r.backRight.setPower(1);
//		r.waiter(3000);
//		r.setToStill();
		
		
		// DO NOT EDIT
		fileManager.close();
	}
	
	
}