package asiankoala.ftc2021.commands.sequences.teleop

import asiankoala.ftc2021.commands.subsystem.ArmCommands
import asiankoala.ftc2021.commands.subsystem.IndexerCommands
import asiankoala.ftc2021.commands.subsystem.IntakeCommands
import asiankoala.ftc2021.commands.subsystem.OuttakeCommands
import asiankoala.ftc2021.subsystems.*
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.WaitCommand
import com.asiankoala.koawalib.command.commands.WaitUntilCommand
import com.asiankoala.koawalib.command.group.ParallelCommandGroup
import com.asiankoala.koawalib.command.group.SequentialCommandGroup

class HomeSequence(turret: Turret, slides: Slides, outtake: Outtake, indexer: Indexer, arm: Arm, intake: Intake) : SequentialCommandGroup(
        ParallelCommandGroup(
                IndexerCommands.IndexerLockCommand(indexer),
                OuttakeCommands.OuttakeCockCommand(outtake),
                ArmCommands.ArmHomeCommand(arm),
                IntakeCommands.IntakeTurnOffCommand(intake)
        ),
        WaitCommand(0.2),
        InstantCommand({slides.motor.followMotionProfile(Slides.slideHomeValue)}),
        WaitCommand(0.5),
        InstantCommand({turret.motor.setPIDTarget(Turret.homeAngle)}),
        WaitUntilCommand { slides.motor.isAtTarget && turret.motor.isAtTarget},
        OuttakeCommands.OuttakeHomeCommand(outtake),
        IndexerCommands.IndexerOpenCommand(indexer)
)