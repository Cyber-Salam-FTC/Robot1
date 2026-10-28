// red for both autos

package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedropathing.Constants;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;

@Autonomous(name = "solo auto", group = "Autonomous")
public class soloAuto extends OpMode {

    private Follower follower;
    private Path[] paths;

    private final ElapsedTime pauseTimer = new ElapsedTime();

    private int pathIndex;
    private boolean waiting;
    private boolean finished;

    private final double[] pauses = {1.0, 0, 0, 0, 1.0, 0};

    private final Pose startPose = new Pose(56, 8, Math.toRadians(90));
    private final Pose line1End = new Pose(56, 24, Math.toRadians(90));
    private final Pose line2End = new Pose(20, 28, Math.toRadians(180));
    private final Pose line3End = new Pose(16, 28, Math.toRadians(180));
    private final Pose line4End = new Pose(16, 28, Math.toRadians(180));
    private final Pose line5End = new Pose(14, 120, Math.toRadians(0));
    private final Pose line6Control = new Pose(25, 110);
    private final Pose line6End = new Pose(45, 132, Math.toRadians(90));
    private final Pose line7End = new Pose(58, 120, Math.toRadians(-90));

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setPose(startPose);

        paths = new Path[] {
                line(startPose, line1End)
                        .linear(
                        Math.toRadians(90),
                        Math.toRadians(90)
                ),

                line(line1End, line2End)
                        .linear(
                        Math.toRadians(90),
                        Math.toRadians(180)
                ),

                line(line2End, line3End)
                        .constant(Math.toRadians(180)),

                line(line3End, line4End)
                        .constant(Math.toRadians(180)),

                line(line4End, line5End)
                        .constant(Math.toRadians(180)),

                curve(line5End, line6Control, line6End)
                        .linear(
                        Math.toRadians(180),
                        Math.toRadians(-90)
                )
        };

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Start Pose", startPose);
        telemetry.addLine("Press PLAY to begin.");
    }

    @Override
    public void start() {
        pathIndex = 0;
        waiting = false;
        finished = false;

        follower.follow(paths[pathIndex]);
    }

    @Override
    public void loop() {
        follower.update();

        if (!finished) {
            updatePaths();
        }

        telemetry.addData("Path", "%d / %d",
                pathIndex + 1, paths.length);
        telemetry.addData("Status",
                finished ? "Finished"
                        : waiting ? "Pausing" : "Following");
        telemetry.addData("Busy", follower.isBusy());
        telemetry.addData("Pose", follower.pose());
        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());

        if (waiting) {
            telemetry.addData("Pause Remaining (s)", "%.2f",
                    Math.max(
                            0,
                            pauses[pathIndex] - pauseTimer.seconds()
                    ));
        }

        telemetry.addLine("Press B or STOP to abort.");
        telemetry.update();
    }

    private void updatePaths() {
        if (waiting) {
            if (pauseTimer.seconds() >= pauses[pathIndex]) {
                advancePath();
            }
            return;
        }

        if (follower.isBusy()) {
            return;
        }

        if (pauses[pathIndex] > 0) {
            waiting = true;
            pauseTimer.reset();
        } else {
            advancePath();
        }
    }

    private void advancePath() {
        waiting = false;

        if (pathIndex == paths.length - 1) {
            finished = true;
            requestOpModeStop();
            return;
        }

        pathIndex++;
        follower.follow(paths[pathIndex]);
    }
}