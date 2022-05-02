package asiankoala.offseason.commands.subsystem

import asiankoala.offseason.subsystems.Duck
import com.asiankoala.koawalib.command.commands.InstantCmd
import com.asiankoala.koawalib.command.commands.WaitCmd
import com.asiankoala.koawalib.command.group.SequentialGroup
import com.asiankoala.koawalib.util.Alliance

object DuckCommands {
    class DuckSetSpeedCommand(duck: Duck, speed: Double) : InstantCmd({duck.motor.power = speed}, duck)
    class DuckSpinSequence(duck: Duck, alliance: Alliance) : SequentialGroup(
            DuckSetSpeedCommand(duck, alliance.decide(0.25, -0.25)),
            WaitCmd(1.3),
            DuckSetSpeedCommand(duck, alliance.decide(1.0, -1.0)),
            WaitCmd(0.3),
            DuckSetSpeedCommand(duck, 0.0)
    )
}