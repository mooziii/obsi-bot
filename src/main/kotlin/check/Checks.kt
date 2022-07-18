package check

import com.kotlindiscord.kord.extensions.checks.types.CheckContext
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Role
import dev.kord.core.event.interaction.InteractionCreateEvent
import me.obsilabor.obsibot.utils.hasRole

@OptIn(KordPreview::class)
suspend fun CheckContext<InteractionCreateEvent>.hasRole(role: Snowflake) {
    if (!passed) {
        return
    }
    if (event.interaction.user.asMember(event.interaction.data.guildId.value ?: error("`hasRole` must always be combinded with `anyGuild`")).hasRole(role)) {
        pass()
    } else {
        fail()
    }
}

suspend fun CheckContext<InteractionCreateEvent>.hasRole(role: Role) {
    hasRole(role.id)
}