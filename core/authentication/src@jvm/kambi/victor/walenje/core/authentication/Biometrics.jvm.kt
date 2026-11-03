package kambi.victor.walenje.core.authentication

actual class BiometricPromptManager : Biometrics {
  actual override suspend fun authenticate(): AuthenticationResult {
    // TODO("Not yet implemented")
    return AuthenticationResult.Success
  }
}
