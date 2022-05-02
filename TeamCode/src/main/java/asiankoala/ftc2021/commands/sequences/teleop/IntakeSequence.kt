package asiankoala.ftc2021.commands.sequences.teleop

import asiankoala.ftc2021.Strategy
import asiankoala.ftc2021.commands.subsystem.IndexerCommands
import asiankoala.ftc2021.commands.subsystem.IntakeCommands
import asiankoala.ftc2021.commands.subsystem.OuttakeCommands
import asiankoala.ftc2021.subsystems.*
import com.asiankoala.koawalib.command.commands.ConditionalCommand
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.WaitCommand
import com.asiankoala.koawalib.command.commands.WaitUntilCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup

class IntakeSequence(strategy: () -> Strategy, intake: Intake, outtake: Outtake, indexer: Indexer, turret: Turret, arm: Arm, slides: Slides) : SequentialCommandGroup(
        OuttakeCommands.OuttakeHomeCommand(outtake)
                .alongWith(IndexerCommands.IndexerOpenCommand(indexer)),
        WaitCommand(0.3),
        IntakeCommands.IntakeTurnOnCommand(intake)
                .alongWith(InstantCommand(intake::startReading)),
        WaitUntilCommand(intake::hasMineral),
        IndexerCommands.IndexerLockCommand(indexer)
                .alongWith(InstantCommand(intake::stopReading)),
        WaitCommand(0.5),
        IntakeCommands.IntakeTurnReverseCommand(intake),
        WaitCommand(0.4),
        InstantCommand({
            outtake.setPosition(strategy.invoke().getOuttakePosition())
        }, outtake)
                .alongWith(InstantCommand({arm.setPosition(strategy.invoke().getArmPosition())}, arm)),
        WaitCommand(0.2),
        InstantCommand({
            val strat = strategy.invoke()
            val angle = strat.getTurretAngle()
            turret.motor.setPIDTarget(angle)
        }, turret).alongWith(
                IntakeCommands.IntakeTurnOffCommand(intake),
                ConditionalCommand(
                            InstantCommand({
                                slides.motor.followMotionProfile(strategy.invoke().getSlideInches())
                            }),
                        InstantCommand({})
                ) {
                    false
                }
        )
)