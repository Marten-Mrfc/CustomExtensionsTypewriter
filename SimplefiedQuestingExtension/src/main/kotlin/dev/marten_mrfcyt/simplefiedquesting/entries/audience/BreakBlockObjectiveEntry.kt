package dev.marten_mrfcyt.simplefiedquesting.entries.audience
// BreakBlockObjectiveEntry.kt
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.entries.ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.entries.*
import com.typewritermc.quest.QuestEntry
import dev.marten_mrfcyt.simplefiedquesting.BaseCountObjectiveDisplay
import dev.marten_mrfcyt.simplefiedquesting.BaseCountObjectiveEntry
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockBreakEvent
import java.util.*

@Entry("break_block_objective", "An objective to break blocks", Colors.BLUE_VIOLET, "mdi:pickaxe")
class BreakBlockObjectiveEntry(
    override val id: String = "",
    override val name: String = "",
    override val quest: Ref<QuestEntry> = emptyRef(),
    override val fact: Ref<CachableFactEntry> = emptyRef(),
    override val children: List<Ref<AudienceEntry>> = emptyList(),
    @Help("The item that the player needs to break.")
    val item: Var<Material> = ConstVar(Material.AIR),
    @Help("The amount of blocks to break.")
    override val amount: Var<Int> = ConstVar(1),
    override val criteria: List<Criteria> = emptyList(),
    override val display: Var<String> = ConstVar(""),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : BaseCountObjectiveEntry {
    override suspend fun display(): AudienceFilter {
        return BreakBlockObjectiveDisplay(ref())
    }
}

private class BreakBlockObjectiveDisplay(ref: Ref<BreakBlockObjectiveEntry>) :
    BaseCountObjectiveDisplay<BreakBlockObjectiveEntry>(ref) {

    @EventHandler(priority = EventPriority.MONITOR)
    fun breakBlockEvent(event: BlockBreakEvent) {
        val player = event.player
        val entry = ref.get() ?: return

        if (!filter(player)) return
        if (event.block.type == entry.item.get(player)) {
            incrementCount(player)
        }
    }
}