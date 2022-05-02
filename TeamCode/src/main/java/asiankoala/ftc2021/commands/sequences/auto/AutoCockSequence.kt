package asiankoala.ftc2021.commands.sequences.auto

import asiankoala.ftc2021.commands.subsystem.ArmCommands
import asiankoala.ftc2021.commands.subsystem.IndexerCommands
import asiankoala.ftc2021.commands.subsystem.IntakeCommands
import asiankoala.ftc2021.commands.subsystem.OuttakeCommands
import asiankoala.ftc2021.subsystems.*
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.WaitCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.util.Alliance

class AutoCockSequence(alliance: Alliance, intake: Intake, outtake: Outtake, indexer: Indexer, turret: Turret, arm: Arm) : SequentialCommandGroup(
        IndexerCommands.IndexerLockCommand(indexer),
        WaitCommand(0.3),
        InstantCommand(intake::turnReverse),
        WaitCommand(0.4),
        OuttakeCommands.OuttakeDepositHighCommand(outtake)
                .alongWith(ArmCommands.ArmDepositHighCommand(arm)),
        InstantCommand({
            val angle = alliance.decide(Turret.autoBlueAngle, Turret.autoRedAngle)
            turret.motor.setPIDTarget(angle)
        }, turret).alongWith(IntakeCommands.IntakeTurnOffCommand(intake))
)