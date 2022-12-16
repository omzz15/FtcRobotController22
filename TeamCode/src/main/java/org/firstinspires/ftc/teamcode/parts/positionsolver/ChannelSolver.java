package org.firstinspires.ftc.teamcode.parts.positionsolver;

import org.firstinspires.ftc.teamcode.parts.drive.DriveControl;
import org.firstinspires.ftc.teamcode.parts.positionsolver.settings.ChannelSolverSettings;
import org.firstinspires.ftc.teamcode.parts.positionsolver.settings.PositionSolverSettings;

import om.self.ezftc.core.part.ControllablePart;

public abstract class ChannelSolver extends Solver<PositionSolver, DriveControl>{
    public ChannelSolver(PositionSolver positionSolver, String name) {
        super(positionSolver, name);
    }

    @Override
    public ControllablePart<?, ?, ?, DriveControl> getControlled() {
        return parent.parent;
    }
}
