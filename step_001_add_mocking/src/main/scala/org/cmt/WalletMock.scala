package org.cmt

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}

object WalletMock {

  def apply(): Behavior[Wallet.Command] =
    Behaviors.setup { context =>
      new WalletMock(context).running(balance = 0)
    }

}

class WalletMock private (context: ActorContext[Wallet.Command]) {
  import Wallet._

  def running(balance: Decimal): Behavior[Wallet.Command] = {
    Behaviors.receiveMessage {
      case DepositMoney(amount, replyTo) =>
        val newBalance = balance + amount
        if (balance > 1000)
          replyTo ! OperationSuccess(newBalance)
        else
          replyTo ! OperationFailed(balance)
        running(newBalance)
      case WithdrawMoney(amount, replyTo) =>
        if (amount <= balance) {
          val newBalance = balance - amount
          replyTo ! OperationSuccess(newBalance)
          running(newBalance)
        }
        else {
          replyTo ! OperationFailed(balance)
          Behaviors.same
        }
      case GetBalance(replyTo) =>
        replyTo ! Balance(balance)
        Behaviors.same
    }
  }
}
