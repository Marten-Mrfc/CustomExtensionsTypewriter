package dev.marten_mrfcyt.simplefiedquesting

import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.engine.paper.entry.entries.CachableFactEntry
import com.typewritermc.engine.paper.entry.entries.Var
import com.typewritermc.engine.paper.entry.matches
import com.typewritermc.engine.paper.extensions.placeholderapi.parsePlaceholders
import com.typewritermc.engine.paper.snippets.snippet
import com.typewritermc.engine.paper.utils.asMini
import com.typewritermc.engine.paper.utils.asMiniWithResolvers
import com.typewritermc.quest.ObjectiveEntry
import com.typewritermc.quest.inactiveObjectiveDisplay
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.parsed
import org.bukkit.entity.Player
import kotlin.math.absoluteValue

private val displaySnippet by snippet(
    "advancedQuest.display",
    "<display> <dark_gray>(<gray><current><dark_gray>/<gray><required><dark_gray>)"
)

private val completedDisplaySnippet by snippet(
    "advancedQuest.completed",
    "<green>✔</green> <st><display></st> <dark_gray>(<gray><current><dark_gray>/<gray><required><dark_gray>)"
)

interface BaseCountObjectiveEntry : ObjectiveEntry {
    @Help("The fact that is used to track the player's progress.")
    val fact: Ref<CachableFactEntry>

    @Help("The required amount to complete the objective.")
    val amount: Var<Int>
    override fun display(player: Player?): String {
        if (player == null) return inactiveObjectiveDisplay

        val factEntry = fact.get()
        val currentValue = factEntry?.readForPlayersGroup(player)?.value ?: 0

        val text = if (!criteria.matches(player)) {
            inactiveObjectiveDisplay
        } else {
            val isComplete = currentValue >= amount.get(player).absoluteValue
            if (isComplete) completedDisplaySnippet else displaySnippet
        }

        val displayText = display.get(player)

        return text.asMiniWithResolvers(
            parsed("display", displayText),
            parsed("current", currentValue.toString()),
            parsed("required", amount.get(player).absoluteValue.toString())
        ).asMini().parsePlaceholders(player)
    }
}

abstract class BaseCountObjectiveDisplay<T : BaseCountObjectiveEntry>(ref: Ref<T>) :
    ObjectiveDisplay<T>(ref) {

    protected fun incrementCount(player: Player, incrementAmount: Int = 1) {
        val entry = ref.get() ?: return
        val fact = entry.fact.get() ?: return

        fact.apply {
            val currentValue = readForPlayersGroup(player).value
            write(player, currentValue + incrementAmount)
        }
    }
}