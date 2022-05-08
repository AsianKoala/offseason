package asiankoala.offseason.commands.sequences.teleop

import asiankoala.offseason.Strategy
import asiankoala.offseason.commands.subsystem.IndexerCommands
import asiankoala.offseason.commands.subsystem.IntakeCommands
import asiankoala.offseason.commands.subsystem.OuttakeCommands
import asiankoala.offseason.subsystems.*
import com.asiankoala.koawalib.command.commands.ChooseCmd
import com.asiankoala.koawalib.command.commands.InstantCmd
import com.asiankoala.koawalib.command.commands.WaitCmd
import com.asiankoala.koawalib.command.commands.WaitUntilCmd
import com.asiankoala.koawalib.command.group.SequentialGroup

class IntakeSequence(strategy: () -> Strategy, intake: Intake, outtake: Outtake, indexer: Indexer, turret: Turret, arm: Arm, slides: Slides) : SequentialGroup(
        OuttakeCommands.OuttakeHomeCommand(outtake)
                .alongWith(IndexerCommands.IndexerOpenCommand(indexer)),
        WaitCmd(0.3),
        IntakeCommands.IntakeTurnOnCommand(intake)
                .alongWith(InstantCmd(intake::startReading)),
        WaitUntilCmd(intake::hasMineral),
        IndexerCommands.IndexerLockCommand(indexer)
                .alongWith(InstantCmd(intake::stopReading)),
        WaitCmd(0.5),
        IntakeCommands.IntakeTurnReverseCommand(intake),
        WaitCmd(0.4),
        InstantCmd({
            outtake.setPosition(strategy.invoke().getOuttakePosition())
        }, outtake)
                .alongWith(InstantCmd({arm.setPosition(strategy.invoke().getArmPosition())}, arm)),
        WaitCmd(0.2),
        InstantCmd({
            val strat = strategy.invoke()
            val angle = strat.getTurretAngle()
            turret.motor.setTarget(angle)
        }, turret).alongWith(
                IntakeCommands.IntakeTurnOffCommand(intake),
                ChooseCmd(
                            InstantCmd({
                                slides.motor.setTarget(strategy.invoke().getSlideInches())
                            }),
                        InstantCmd({})
                ) {
                    false
                }
        )
)