package org.cmt

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import org.cmt.Wallet.Decimal

object WalletHolder {
  // My Protocol
  // Commands
  sealed trait Command
  final case class SpendOrEarn(earn: Boolean, amount: Decimal) extends Command
  // Response wrappers
  private final case class WalletResponseWrapper(response: Wallet.Response) extends Command

  // Responses - We're not responding, so this is empty...

  def apply(wallet: ActorRef[Wallet.Command]): Behavior[WalletHolder.Command] =
    Behaviors.setup { context =>
      new WalletHolder(context, wallet).operational()
    }
}

class WalletHolder private (context: ActorContext[WalletHolder.Command], wallet: ActorRef[Wallet.Command]) {

  import WalletHolder._

  private val log = context.log

  private val walletResponseAdapter: ActorRef[Wallet.Response] =
    context.messageAdapter(walletResponse => WalletResponseWrapper(walletResponse))

  def operational(): Behavior[WalletHolder.Command] =
    Behaviors.receiveMessage {
      case SpendOrEarn(true, amount) =>
        wallet ! Wallet.DepositMoney(amount, walletResponseAdapter)
        Behaviors.same
      case SpendOrEarn(false, amount) =>
        wallet ! Wallet.WithdrawMoney(amount, walletResponseAdapter)
        Behaviors.same
      case WalletResponseWrapper(walletResponse) =>
        walletResponse match {
          case Wallet.OperationSuccess(balance) =>
            log.info("Ok! - Balance = {}", balance)
            Behaviors.same
          case Wallet.OperationFailed(balance) =>
            log.info("Not ok! - Balance = {}", balance)
            Behaviors.same
          case Wallet.Balance(balance) =>
            log.info("New Balance = {}", balance)
            Behaviors.same
        }
    }
}
