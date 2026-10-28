package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedropathing.Constants;

import static com.pedropathing.api.Paths.line;

@Autonomous(name = "Forward Test")
public class lineTest extends OpMode {

    private Follower follower;
    private Path forwardPath;

    private final PoseFactory p = PoseFactory.degrees();
    private final Pose startPose = p.of(0, 0, 0);
    private final Pose endPose = p.of(24, 0, 0);

    private final ElapsedTime timer = new ElapsedTime();
    private String status = "Initialized";

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setPose(startPose);

        forwardPath = line(startPose, endPose)
                .linear(startPose, endPose);

        telemetry.addData("Status", status);
        telemetry.addLine("Will move forward 24 inches.");
    }

    @Override
    public void start() {
        timer.reset();
        status = "Following";
        follower.follow(forwardPath);
    }

    @Override
    public void loop() {
        // Driver-controlled abort.
        if (gamepad1.b) {
            status = "Aborted";
            requestOpModeStop();
            return;
        }

        // Prevent the test from running indefinitely.
        if (timer.seconds() > 8.0) {
            status = "Timed out";
            requestOpModeStop();
            return;
        }

        follower.update();

        if (!follower.isBusy()) {
            status = "Finished";
            requestOpModeStop();
        }

        telemetry.addData("Status", status);
        telemetry.addData("Busy", follower.isBusy());
        telemetry.addData("Pose", follower.pose());
        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Time (s)", "%.2f", timer.seconds());
        telemetry.addLine("Press B or STOP to abort.");
        telemetry.update();
    }
}