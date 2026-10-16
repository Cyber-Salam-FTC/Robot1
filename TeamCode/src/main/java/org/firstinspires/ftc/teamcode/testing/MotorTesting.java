package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class MotorTesting extends LinearOpMode {
    @Override
    public void runOpMode() {
        DcMotor testMotor = hardwareMap.get(DcMotor.class, "testMotor");

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a) {
                testMotor.setPower(1);
            }

            if (gamepad1.b) {
                testMotor.setPower(-1);
            }
        }
    }
}
