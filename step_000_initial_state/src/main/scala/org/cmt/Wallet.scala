package org.cmt

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}

object Wallet {
  type Decimal = BigDecimal

  // Wallet protocol
  // Wallet commands
  sealed trait Command
  final case class DepositMoney(amount: Decimal, replyTo: ActorRef[Wallet.Response]) extends Command
  final case class WithdrawMoney(amount: Decimal, replyTo: ActorRef[Wallet.Response]) extends Command
  case class GetBalance(replyTo: ActorRef[Wallet.Response]) extends Command

  // Wallet Responses
  sealed trait Response
  final case class Balance(balance: Decimal) extends Response
  final case class OperationSuccess(balance: Decimal) extends Response
  final case class OperationFailed(balance: Decimal) extends Response

  def apply(): Behavior[Command] =
    Behaviors.setup { context =>
      new Wallet(context).running(balance = 0)
    }
}

class Wallet private (context: ActorContext[Wallet.Command]) {
  import Wallet._

  def running(balance: Decimal): Behavior[Wallet.Command] = {
    Behaviors.receiveMessage {
      case DepositMoney(amount, replyTo) =>
        val newBalance = balance + amount
        replyTo ! OperationSuccess(newBalance)
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