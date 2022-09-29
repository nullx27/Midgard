package tech.grimm.midgard.commands

import kotlinx.datetime.Clock
import me.jakejmattson.discordkt.arguments.ChoiceArg
import me.jakejmattson.discordkt.arguments.DoubleArg
import me.jakejmattson.discordkt.commands.commands
import me.jakejmattson.discordkt.extensions.addInlineField
import tech.grimm.midgard.data.Currencies
import tech.grimm.midgard.services.CurrencyExchangeService
import tech.grimm.midgard.services.Permissions

fun exchangeRate(exchangeService: CurrencyExchangeService) = commands("Utility") {
    slash("exchange", "Currency Exchange Rates", Permissions.EVERYONE) {
        execute(
            DoubleArg("Amount", "Currency Amount"),
            ChoiceArg("From", "Base Currency", *Currencies.values()),
            ChoiceArg("To", "Foreign Currency", *Currencies.values())

        ) {
            val exchangeRate =
                exchangeService.getExchangeRate(args.first, args.second.toString(), args.third.toString())

            respondPublic {
                title = "Exchange ${args.second} to ${args.third}"
                addInlineField(args.second.toString(), args.first.toString())
                addInlineField(args.third.toString(), String.format("%.2f", exchangeRate.rates.entries.first().value))
                timestamp = Clock.System.now()
            }
        }
    }
}