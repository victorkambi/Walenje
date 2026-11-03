package kambi.victor.walenje

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import kambi.victor.walenje.core.authentication.Biometrics
import kambi.victor.walenje.core.designsystem.theme.WalenjeTheme
import kambi.victor.walenje.di.createKoinConfiguration
import kambi.victor.walenje.navigation.WalenjeNavGraph
import org.koin.compose.KoinApplication

@Composable
fun App(biometrics: Biometrics) {
  KoinApplication(configuration = createKoinConfiguration()) {
    /* Apply Surface to make the icons visible with edgeToEdge */
    WalenjeTheme { Surface { WalenjeNavGraph(biometrics = biometrics) } }
  }
}
