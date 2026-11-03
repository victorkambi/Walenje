package kambi.victor.walenje.core.authentication

import kotlin.coroutines.resume
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAErrorBiometryLockout
import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthenticationWithBiometrics

/** Touch ID through LocalAuthentication. */
actual class BiometricPromptManager : Biometrics {
  @OptIn(ExperimentalForeignApi::class)
  actual override suspend fun authenticate(): AuthenticationResult =
    suspendCancellableCoroutine { continuation ->
      val context = LAContext()
      if (
        !context.canEvaluatePolicy(LAPolicyDeviceOwnerAuthenticationWithBiometrics, error = null)
      ) {
        continuation.resume(AuthenticationResult.Error("Touch ID is not available on this Mac"))
        return@suspendCancellableCoroutine
      }
      continuation.invokeOnCancellation { context.invalidate() }
      context.evaluatePolicy(
        LAPolicyDeviceOwnerAuthenticationWithBiometrics,
        localizedReason = "unlock Walenje",
      ) { success, error ->
        val result =
          when {
            success -> AuthenticationResult.Success
            error?.code == LAErrorBiometryLockout -> AuthenticationResult.AttemptExhausted
            else ->
              AuthenticationResult.Error(error?.localizedDescription ?: "Authentication failed")
          }
        if (continuation.isActive) continuation.resume(result)
      }
    }
}
