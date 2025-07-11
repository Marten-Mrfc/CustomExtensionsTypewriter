package dev.marten_mrfcyt.simplefiedquesting.entries.audience
// KillEntityObjectiveEntry.kt
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
import org.bukkit.event.entity.EntityDeathEvent
import java.util.*

@Entry("kill_entity_objective", "An objective to kill entities", Colors.BLUE_VIOLET, "game-icons:backstab")
class KillEntityObjectiveEntry(
    override val id: String = "",
    override val name: String = "",
    override val quest: Ref<QuestEntry> = emptyRef(),
    override val criteria: List<Criteria> = emptyList(),
    override val children: List<Ref<AudienceEntry>> = emptyList(),
    override val fact: Ref<CachableFactEntry> = emptyRef(),
    @Help("The entity type that the player needs to kill. If empty, any entity type will count.")
    val entityType: Optional<EntityType> = Optional.empty(),
    @Help("The amount of entities to kill.")
    override val amount: Var<Int> = ConstVar(1),
    override val display: Var<String> = ConstVar(""),
    override val onComplete: Ref<TriggerableEntry> = emptyRef(),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : BaseCountObjectiveEntry {
    override suspend fun display(): AudienceFilter {
        return KillEntityObjectiveDisplay(ref())
    }
}

private class KillEntityObjectiveDisplay(ref: Ref<KillEntityObjectiveEntry>) :
    BaseCountObjectiveDisplay<KillEntityObjectiveEntry>(ref) {

    @EventHandler(priority = EventPriority.MONITOR)
    fun entityDeathEvent(event: EntityDeathEvent) {
        val killer = event.entity.killer ?: return
        val entry = ref.get() ?: return

        if (!filter(killer)) return

        if (!entry.entityType.isPresent || entry.entityType.get() == event.entityType) {
            incrementCount(killer)
        }
    }
}