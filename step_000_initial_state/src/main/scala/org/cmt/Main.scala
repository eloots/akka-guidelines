package org.cmt

import akka.NotUsed
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior, Terminated}
import org.slf4j.Logger

import scala.util.Random

object ActorMain {
  def sendRandomOperations(walletHolder: ActorRef[WalletHolder.Command], log: Logger): Unit = {
    Random.setSeed(123456)

    for { _ <- 1 to 25} {
      val amount = 1 + Random.nextInt(100)
      val operation = Random.nextBoolean()
      log.info("Sending SpendOrEarn(earn = {}, {})", operation, amount)
      walletHolder ! WalletHolder.SpendOrEarn(operation, amount)
    }

  }
  def apply(): Behavior[NotUsed]  =
    Behaviors.setup { context =>
      val wallet = context.spawn(Wallet(), "wallet")
      val walletHolder = context.spawn(WalletHolder(wallet),"wallet-holder")

      sendRandomOperations(walletHolder, context.log)

      Behaviors.receiveSignal {
        case (_, Terminated(_)) =>
          Behaviors.stopped
      }
    }
}

object Main {

  def main(args: Array[String]): Unit = {

    val system = ActorSystem[NotUsed](ActorMain(), "guidelines-system")


    system.terminate()
  }
}
