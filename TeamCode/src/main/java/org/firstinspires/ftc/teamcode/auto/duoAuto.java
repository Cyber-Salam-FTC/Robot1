// red for both autos

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

@Autonomous(name = "duo auto", group = "Autonomous")
public class duoAuto extends OpMode {

    private Follower follower;
    private Path[] paths;

    private final ElapsedTime pauseTimer = new ElapsedTime();

    private int pathIndex;
    private boolean waiting;
    private boolean finished;

    private final double[] pauses = {0.5, 0, 0, 0, 0, 0};

    private final PoseFactory p = PoseFactory.degrees();

    private final Pose startPose = p.of(56, 8, 90);
    private final Pose line1End = p.of(56, 24, 90);
    private final Pose line2End = p.of(20, 28, 180);
    private final Pose line3End = p.of(5, 28, 180);
    private final Pose line4End = p.of(16, 28, 180);
    private final Pose line5End = p.of(30, 120, 0);
    private final Pose line6End = p.of(58, 120, -90);

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setPose(startPose);

        paths = new Path[] {
                line(startPose, line1End)
                        .linear(startPose, line1End),

                line(line1End, line2End)
                        .linear(line1End, line2End),

                line(line2End, line3End)
                        .linear(line2End, line3End),

                line(line3End, line4End)
                        .linear(line3End, line4End),

                line(line4End, line5End)
                        .linear(line4End, line5End),

                line(line5End, line6End)
                        .linear(line5End, line6End)
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
        if (gamepad1.b) {
            requestOpModeStop();
            return;
        }

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