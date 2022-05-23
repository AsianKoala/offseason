package asiankoala.ftc2021.commands.sequences.teleop

import asiankoala.offseason.commands.subsystem.ArmCommands
import asiankoala.offseason.commands.subsystem.IndexerCommands
import asiankoala.offseason.commands.subsystem.IntakeCommands
import asiankoala.offseason.commands.subsystem.OuttakeCommands
import asiankoala.offseason.subsystems.*
import com.asiankoala.koawalib.command.commands.*
import com.asiankoala.koawalib.command.group.ParallelGroup
import com.asiankoala.koawalib.command.group.SequentialGroup

class HomeSequence(turret: Turret, slides: Slides, outtake: Outtake, indexer: Indexer, arm: Arm, intake: Intake) : SequentialGroup(
        ParallelGroup(
                IndexerCommands.IndexerLockCommand(indexer),
                OuttakeCommands.OuttakeCockCommand(outtake),
                ArmCommands.ArmHomeCommand(arm),
                IntakeCommands.IntakeTurnOffCommand(intake)
        ),
        WaitCmd(0.2),
        InstantCmd({slides.motor.setTarget(Slides.slideHomeValue)}),
        WaitCmd(0.5),
        InstantCmd({turret.motor.setTarget(Turret.homeAngle)}),
        WaitUntilCmd { slides.motor.isAtTarget() && turret.motor.isAtTarget() },
        OuttakeCommands.OuttakeHomeCommand(outtake),
        IndexerCommands.IndexerOpenCommand(indexer)
)