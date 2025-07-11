package dev.marten_mrfcyt.simplefiedquesting.entries.audience
// EnchantObjectiveEntry.kt
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
import dev.marten_mrfcyt.simplefiedquesting.enums.EnchantmentType
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.enchantment.EnchantItemEvent
import java.util.*

@Entry("enchant_objective", "An objective to enchant items", Colors.BLUE_VIOLET, "ph:magic-wand-bold")
class EnchantObjectiveEntry(
    override val id: String = "",
    override val name: String = "",
    override val quest: Ref<QuestEntry> = emptyRef(),
    override val criteria: List<Criteria> = emptyList(),
    override val children: List<Ref<AudienceEntry>> = emptyList(),
    override val fact: Ref<CachableFactEntry> = emptyRef(),
    @Help("The enchantment needed to be applied.")
    val enchantment: Optional<EnchantmentType> = Optional.empty(),
    @Help("The amount of times the player needs to enchant.")
    override val amount: Var<Int> = ConstVar(0),
    override val display: Var<String> = ConstVar(""),
    override val onComplete: Ref<TriggerableEntry> = emptyRef(),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : BaseCountObjectiveEntry {
    override suspend fun display(): AudienceFilter {
        return EnchantObjectiveDisplay(ref())
    }
}

private class EnchantObjectiveDisplay(ref: Ref<EnchantObjectiveEntry>) :
    BaseCountObjectiveDisplay<EnchantObjectiveEntry>(ref) {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onEnchant(event: EnchantItemEvent) {
        val player = event.enchanter
        val entry = ref.get() ?: return
        if (!filter(player)) return

        val enchantmentType = entry.enchantment
        if (!enchantmentType.isPresent || event.enchantsToAdd.keys.any {
                enchantmentType.isPresent && EnchantmentType.fromEnchantment(
                    it
                ) == enchantmentType.get()
            }) {
            incrementCount(player)
        }
    }
}