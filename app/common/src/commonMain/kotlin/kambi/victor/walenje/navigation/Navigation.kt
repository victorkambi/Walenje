package kambi.victor.walenje.navigation

import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.metadata
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kambi.victor.walenje.core.authentication.Biometrics
import kambi.victor.walenje.feature.home.HomeScreen
import kambi.victor.walenje.feature.welcome.SecureYourWalletScreen
import kambi.victor.walenje.feature.welcome.SetPinScreen
import kambi.victor.walenje.feature.welcome.Welcome
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

sealed interface Route : NavKey {
  @Serializable data object Welcome : Route

  @Serializable data object SecureWallet : Route

  @Serializable data object SetPin : Route

  @Serializable data object Home : Route

  @Serializable data object Analytics : Route

  @Serializable data object Profile : Route
}

/**
 * Only Android can find the [NavKey] subclasses by reflection; the other targets need them
 * registered to save and restore the back stack.
 */
private val routeSavedStateConfiguration = SavedStateConfiguration {
  serializersModule = SerializersModule {
    polymorphic(NavKey::class) {
      subclass(Route.Welcome::class)
      subclass(Route.SecureWallet::class)
      subclass(Route.SetPin::class)
      subclass(Route.Home::class)
      subclass(Route.Analytics::class)
      subclass(Route.Profile::class)
    }
  }
}

@Composable
fun WalenjeNavGraph(biometrics: Biometrics, startDestination: Route = Route.SetPin) {
  val backStack = rememberNavBackStack(routeSavedStateConfiguration, startDestination)

  /* Keep the last destination: leaving the app is the system's job, not ours. */
  fun goBack() {
    if (backStack.size > 1) backStack.removeLastOrNull()
  }

  NavDisplay(
    backStack = backStack,
    onBack = ::goBack,
    entryDecorators =
      listOf(
        rememberSaveableStateHolderNavEntryDecorator(),
        rememberViewModelStoreNavEntryDecorator(),
      ),
    entryProvider =
      entryProvider {
        entry<Route.Welcome> {
          Welcome(onNavigateToSecureWallet = { backStack.add(Route.SecureWallet) })
        }
        entry<Route.SecureWallet> {
          SecureYourWalletScreen(onNavigateToNext = { backStack.add(Route.SetPin) })
        }
        entry<Route.SetPin>(
          metadata =
            metadata {
              put(NavDisplay.TransitionKey) {
                slideInVertically(initialOffsetY = { it }) togetherWith
                  ExitTransition.KeepUntilTransitionsFinished
              }
            },
        ) {
          SetPinScreen(
            onNavigateBack = ::goBack,
            onNavigateToHomeScreen = {
              /* Leave onboarding: drop Welcome and everything above it, then show Home. */
              val welcome = backStack.indexOf(Route.Welcome)
              if (welcome >= 0) backStack.subList(welcome, backStack.size).clear()
              backStack.add(Route.Home)
            },
          )
        }
        entry<Route.Home>(
          metadata =
            metadata {
              put(NavDisplay.TransitionKey) {
                slideInHorizontally(initialOffsetX = { it }) togetherWith
                  ExitTransition.KeepUntilTransitionsFinished
              }
            },
        ) {
          HomeScreen(onNavigateBack = ::goBack)
        }
        entry<Route.Analytics> {}
        entry<Route.Profile> {}
      },
  )
}
