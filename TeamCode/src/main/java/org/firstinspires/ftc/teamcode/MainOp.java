package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.cybersalam.hardware.MecanumDrive;

public class MainOp extends LinearOpMode {
    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive();

        drive.init(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            double forward = gamepad1.right_trigger - gamepad1.left_trigger;
            double strafe = gamepad1.right_stick_x;
            double rotate = gamepad1.left_stick_x;

            drive.drive(forward, strafe, rotate);
        }
    }
}
