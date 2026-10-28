package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.cybersalam.hardware.MecanumDrive;

@TeleOp(name = "mainop")
public class MainOp extends LinearOpMode {

    DcMotor intakeMotor, shooterMotor;
    CRServo intakeServo1, intakeServo2, transferServo;

    @Override
    public void runOpMode() {

    // intake: 2 servos + 1 motor, transfer: 1 servo, shooter: 1 motor

    intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
    intakeServo1 = hardwareMap.get(CRServo.class, "intakeServo1");
    intakeServo2 = hardwareMap.get(CRServo.class, "intakeServo2");

    transferServo = hardwareMap.get(CRServo.class, "transferServo");
    shooterMotor = hardwareMap.get(DcMotor.class, "shooterMotor");


//        MecanumDrive drive = new MecanumDrive();

//        drive.init(hardwareMap, true);

        ControllerState state = ControllerState.NEUTRAL;

        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        waitForStart();

        while (opModeIsActive()) {
//            double forward = gamepad1.right_trigger - gamepad1.left_trigger;
//            double strafe = gamepad1.left_stick_x;
//            double rotate = gamepad1.right_stick_x;
//
//            drive.drive(forward, strafe, rotate);
            transferServo.setPower(1);
            switch (state) {
                case INTAKE_IN:
                    setIntake(1);
                case INTAKE_OUT:
                    setIntake(-1);
                case INTAKE_STOP:
                    setIntake(0);
                case SHOOT_RUN:
                    shooterMotor.setPower(1);
                case SHOOT_STOP:
                    shooterMotor.setPower(0);
                default:
                    setIntake(0);
            }
            
            if (gamepad1.cross) {
                state = ControllerState.INTAKE_IN;
            }

            if (gamepad1.square) {
                state = ControllerState.INTAKE_OUT;
            }

            if (gamepad1.circle) {
                state = ControllerState.INTAKE_STOP;
            }

            if (gamepad1.dpad_up) {
                state = ControllerState.SHOOT_RUN;
            }

            if (gamepad1.dpad_down) {
                state = ControllerState.SHOOT_STOP;
            }
        }
    }

    void setIntake(double power) {
        intakeServo1.setPower(power);
        intakeServo2.setPower(-power);
        intakeMotor.setPower(1);
    }
}
