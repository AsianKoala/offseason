package asiankoala.reiko.commands.sequences

import asiankoala.reiko.commands.subsystem.ArmCommands
import asiankoala.reiko.commands.subsystem.IndexerCommands
import asiankoala.reiko.commands.subsystem.IntakeCommands
import asiankoala.reiko.commands.subsystem.OuttakeCommands
import asiankoala.offseason.subsystems.*
import asiankoala.reiko.subsystems.Arm
import asiankoala.reiko.subsystems.Indexer
import asiankoala.reiko.subsystems.Intake
import asiankoala.reiko.subsystems.Outtake
import com.asiankoala.koawalib.command.commands.*
import com.asiankoala.koawalib.command.group.ParallelGroup
import com.asiankoala.koawalib.command.group.SequentialGroup

class HomeSequence(turret: asiankoala.reiko.subsystems.Turret, slides: asiankoala.reiko.subsystems.Slides, outtake: Outtake, indexer: Indexer, arm: Arm, intake: Intake) : SequentialGroup(
        ParallelGroup(
                IndexerCommands.IndexerLockCommand(indexer),
                OuttakeCommands.OuttakeCockCommand(outtake),
                ArmCommands.ArmHomeCommand(arm),
                IntakeCommands.IntakeTurnOffCommand(intake)
        ),
        WaitCmd(0.2),
        InstantCmd({slides.motor.setProfileTarget(Slides.slideHomeValue)}),
        WaitCmd(0.5),
        InstantCmd({turret.motor.setTargetPosition(Turret.homeAngle)}),
        WaitUntilCmd { slides.motor.isAtTarget() && turret.motor.isAtTarget() },
        OuttakeCommands.OuttakeHomeCommand(outtake),
        IndexerCommands.IndexerOpenCommand(indexer)
)