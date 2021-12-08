package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class MecDrive extends LinearOpMode {


    DcMotor lf = null;
    DcMotor rf = null;
    DcMotor lb = null;
    DcMotor rb = null;
    
    DcMotor lift, inout;

    @Override
    public void runOpMode() {

        lf = hardwareMap.get(DcMotor.class, "FLM");
        rf = hardwareMap.get(DcMotor.class, "FRM");
        lb = hardwareMap.get(DcMotor.class, "BLM");
        rb = hardwareMap.get(DcMotor.class, "BRM");
        
        lift = hardwareMap.dcMotor.get("lift");
        inout = hardwareMap.dcMotor.get("inout");
        
        lf.setDirection(DcMotorSimple.Direction.FORWARD);
        lb.setDirection(DcMotorSimple.Direction.FORWARD); 
        rf.setDirection(DcMotorSimple.Direction.REVERSE);
        rb.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            telemetry.update();

            double jy = -gamepad1.left_stick_y - gamepad1.right_stick_y;
            double jx = gamepad1.left_stick_x;
            double jw = gamepad1.right_stick_x;

            lf.setPower(jy + jx + jw);
            rf.setPower(jy - jx - jw);
            lb.setPower(jy - jx + jw);
            rb.setPower(jy + jx - jw);
            
            lift.setPower(gamepad1.right_stick_y);
            
            if(gamepad1.dpad_up)
                inout.setPower(0.6);
            else if(gamepad1.dpad_down)
                inout.setPower(-0.6);
            else inout.setPower(0);

        }
    }
}

 
