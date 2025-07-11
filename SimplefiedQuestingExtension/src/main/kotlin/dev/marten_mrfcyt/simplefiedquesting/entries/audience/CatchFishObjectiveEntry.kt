package dev.marten_mrfcyt.simplefiedquesting.entries.audience
// CatchFishObjectiveEntry.kt
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
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerFishEvent
import java.util.*

@Entry("catch_fish_objective", "An objective to catch fish", Colors.BLUE_VIOLET, "mdi:fish")
class CatchFishObjectiveEntry(
    override val id: String = "",
    override val name: String = "",
    override val quest: Ref<QuestEntry> = emptyRef(),
    override val criteria: List<Criteria> = emptyList(),
    override val children: List<Ref<AudienceEntry>> = emptyList(),
    override val fact: Ref<CachableFactEntry> = emptyRef(),
    @Help("The entity type that the player needs to catch. If empty, any fish type will count.")
    val entityType: Optional<EntityType> = Optional.empty(),
    @Help("The amount of fish to catch.")
    override val amount: Var<Int> = ConstVar(0),
    override val display: Var<String> = ConstVar(""),
    override val onComplete: Ref<TriggerableEntry> = emptyRef(),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : BaseCountObjectiveEntry {
    override suspend fun display(): AudienceFilter {
        return CatchFishObjectiveDisplay(ref())
    }
}

private class CatchFishObjectiveDisplay(ref: Ref<CatchFishObjectiveEntry>) :
    BaseCountObjectiveDisplay<CatchFishObjectiveEntry>(ref) {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerFish(event: PlayerFishEvent) {
        val player = event.player
        val entry = ref.get() ?: return
        val fishType = entry.entityType.orElse(null)
        if (!filter(player)) return
        if (event.state == PlayerFishEvent.State.CAUGHT_FISH && event.caught?.type == fishType || fishType == null) {
            incrementCount(player)
        }
    }
}