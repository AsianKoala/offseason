package asiankoala.offseason.commands.subsystem

import asiankoala.offseason.subsystems.Indexer
import com.asiankoala.koawalib.command.commands.InstantCmd

object IndexerCommands {
    class IndexerOpenCommand(indexer: Indexer) : InstantCmd(indexer::open, indexer)
    class IndexerLockCommand(indexer: Indexer) : InstantCmd(indexer::lock, indexer)
    class IndexerIndexCommand(indexer: Indexer) : InstantCmd(indexer::index, indexer)
}