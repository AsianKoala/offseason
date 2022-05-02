package asiankoala.offseason.commands.subsystem

import asiankoala.offseason.subsystems.Intake
import com.asiankoala.koawalib.command.commands.InstantCmd
import com.asiankoala.koawalib.command.commands.WaitUntilCmd

object IntakeCommands {
    class IntakeTurnOnCommand(intake: Intake) : InstantCmd(intake::turnOn)
    class IntakeTurnOffCommand(intake: Intake) : InstantCmd(intake::turnOff)
    class IntakeTurnReverseCommand(intake: Intake) : InstantCmd(intake::turnReverse)
    class IntakeStartReadingCommand(intake: Intake) : InstantCmd(intake::startReading)
    class IntakeStopReadingCommand(intake: Intake) : InstantCmd(intake::stopReading)
    class IntakeHasMineralCommand(intake: Intake) : WaitUntilCmd(intake::hasMineral)
}
