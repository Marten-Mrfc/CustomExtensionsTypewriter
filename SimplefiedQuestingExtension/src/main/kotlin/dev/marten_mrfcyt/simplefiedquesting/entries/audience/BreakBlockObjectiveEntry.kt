package dev.marten_mrfcyt.simplefiedquesting.entries.audience

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.entries.ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.*
import com.typewritermc.quest.QuestEntry
import dev.marten_mrfcyt.simplefiedquesting.BaseCountObjectiveDisplay
import dev.marten_mrfcyt.simplefiedquesting.BaseCountObjectiveEntry
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.util.Vector
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Entry("break_block_objective", "An objective to break blocks", Colors.BLUE_VIOLET, "mdi:pickaxe")
class BreakBlockObjectiveEntry(
    override val id: String = "",
    override val name: String = "",
    override val quest: Ref<QuestEntry> = emptyRef(),
    override val criteria: List<Criteria> = emptyList(),
    override val fact: Ref<CachableFactEntry> = emptyRef(),
    override val children: List<Ref<AudienceEntry>> = emptyList(),
    @Help("The item that the player needs to break.")
    val item: Var<Material> = ConstVar(Material.AIR),
    @Help("The amount of blocks to break.")
    override val amount: Var<Int> = ConstVar(1),
    override val display: Var<String> = ConstVar(""),
    override val onComplete: Ref<TriggerableEntry> = emptyRef(),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : BaseCountObjectiveEntry {
    override suspend fun display(): AudienceFilter {
        return BreakBlockObjectiveDisplay(ref())
    }
}

private class BreakBlockObjectiveDisplay(ref: Ref<BreakBlockObjectiveEntry>) :
    BaseCountObjectiveDisplay<BreakBlockObjectiveEntry>(ref) {

    private val minedBlocks = ConcurrentHashMap.newKeySet<Vector>()

    @EventHandler(priority = EventPriority.MONITOR)
    fun breakBlockEvent(event: BlockBreakEvent) {
        val player = event.player
        val entry = ref.get() ?: return
        if (!filter(player)) return
        val expected = entry.item.get(player)
        val block = event.block
        if (block.type != expected) return

        val loc = block.location.toVector()
        if (!minedBlocks.add(loc)) return

        incrementCount(player)
    }
}
