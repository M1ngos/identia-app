package com.identia.app.core.i18n

import androidx.compose.runtime.staticCompositionLocalOf

/** Supported UI languages. [label] is shown verbatim in the Settings switcher. */
enum class Language(val code: String, val label: String) {
    English("EN", "English"),
    Portuguese("PT", "Português"),
}

/**
 * All user-facing copy for one language. Add a key here, then implement it in
 * both [En] and [Pt]. Interpolated copy is exposed as functions.
 */
interface Strings {
    // Common / actions
    val done: String
    val tryAgain: String
    val continueAction: String
    val contactSupport: String
    val logOut: String

    // Bottom navigation tabs
    val tabHome: String
    val tabVerify: String
    val tabFace: String
    val tabLogs: String

    // Auth entry
    val tagline: String
    val enterAccessCode: String
    val invalidCode: String
    val verify: String
    val verifying: String
    fun endToEndEncrypted(version: String): String

    // Home
    val welcomeBack: String
    val identityVerified: String
    fun trustScoreValid(score: Int): String
    val modules: String
    val moduleIdentityVerification: String
    val moduleFaceAuthentication: String
    val moduleLogsAudit: String
    val settings: String

    // Verify · start
    val verifyIdentity: String
    val confirmItsYou: String
    val confirmItsYouSubtitle: String
    val stepScanId: String
    val stepTakeSelfie: String
    val stepLiveness: String
    val secureDuration: String
    val startVerification: String

    // Verify · capture id
    val scanIdFront: String
    val scanIdBack: String
    val alignFrontOfId: String
    val front: String
    val back: String

    // Verify · selfie
    val takeASelfie: String
    val centerYourFace: String
    val goodLightingDetected: String

    // Verify · liveness
    val livenessCheck: String
    val turnYourHead: String
    val keepFaceInsideCircle: String
    val blinkDetected: String
    val smileDetected: String
    val turnHeadDetected: String
    val turnHeadInProgress: String

    // Verify · result
    val yourDocumentMatched: String
    val trustScoreOutOf100: String
    val verificationFailed: String
    val couldntConfirmMatch: String
    val issueBlurry: String
    val issueFaceBelowThreshold: String
    val issueLivenessInconclusive: String
    val fieldName: String
    val fieldDocument: String
    val fieldFaceMatch: String

    // Face · camera / scanning / match
    val faceAuthentication: String
    val positionYourFace: String
    val scanFace: String
    val scanningFace: String
    val analyzingDataPoints: String
    val holdStill: String
    val matchConfirmed: String
    val welcomeBackName: String
    val fieldThreshold: String
    val fieldLiveness: String
    val fieldLatency: String
    val passed: String

    // Logs
    val auditLog: String
    val filterAll: String
    val filterSuccess: String
    val filterFailed: String
    val filterDate: String
    val today: String

    // Settings
    val apiConfiguration: String
    val endpointDemo: String
    fun connectedLatency(ms: Int): String
    val preferences: String
    val theme: String
    val darkMode: String
    val biometricUnlock: String
    val faceIdOnLaunch: String
    val simulateFailure: String
    val forceVerificationToFail: String
    val language: String
    val chooseLanguage: String
    val demoData: String
    val resetDemoData: String
    val session: String
    val signedInAs: String
}

object En : Strings {
    override val done = "Done"
    override val tryAgain = "Try Again"
    override val continueAction = "Continue"
    override val contactSupport = "Contact Support"
    override val logOut = "Log Out"

    override val tabHome = "Home"
    override val tabVerify = "Verify"
    override val tabFace = "Face"
    override val tabLogs = "Logs"

    override val tagline = "VERIFY · AUTHENTICATE · TRUST"
    override val enterAccessCode = "ENTER ACCESS CODE"
    override val invalidCode = "Invalid code. Try again."
    override val verify = "Verify"
    override val verifying = "Verifying…"
    override fun endToEndEncrypted(version: String) = "END-TO-END ENCRYPTED · $version"

    override val welcomeBack = "Welcome back"
    override val identityVerified = "Identity Verified"
    override fun trustScoreValid(score: Int) = "Trust score $score · valid"
    override val modules = "Modules"
    override val moduleIdentityVerification = "Identity\nVerification"
    override val moduleFaceAuthentication = "Face\nAuthentication"
    override val moduleLogsAudit = "Logs / Audit"
    override val settings = "Settings"

    override val verifyIdentity = "Verify Identity"
    override val confirmItsYou = "Let's confirm it's really you"
    override val confirmItsYouSubtitle =
        "A quick three-step check. Have your government ID ready and find good lighting."
    override val stepScanId = "Scan your ID document"
    override val stepTakeSelfie = "Take a quick selfie"
    override val stepLiveness = "Pass the liveness check"
    override val secureDuration = "≈ 60 seconds · secure"
    override val startVerification = "Start Verification"

    override val scanIdFront = "Scan ID — Front"
    override val scanIdBack = "Scan ID — Back"
    override val alignFrontOfId = "Align front of ID within frame"
    override val front = "Front"
    override val back = "Back"

    override val takeASelfie = "Take a Selfie"
    override val centerYourFace = "Center your face in the oval"
    override val goodLightingDetected = "Good lighting detected"

    override val livenessCheck = "Liveness Check"
    override val turnYourHead = "Slowly turn your head →"
    override val keepFaceInsideCircle = "Keep your face inside the circle"
    override val blinkDetected = "Blink detected"
    override val smileDetected = "Smile detected"
    override val turnHeadDetected = "Turn head detected"
    override val turnHeadInProgress = "Turn head — in progress"

    override val yourDocumentMatched = "Your document and biometrics matched successfully."
    override val trustScoreOutOf100 = "/100 trust score"
    override val verificationFailed = "Verification Failed"
    override val couldntConfirmMatch = "We couldn't confirm a match. Please review the issues below."
    override val issueBlurry = "Document photo too blurry"
    override val issueFaceBelowThreshold = "Face match below threshold (61%)"
    override val issueLivenessInconclusive = "Liveness inconclusive"
    override val fieldName = "Name"
    override val fieldDocument = "Document"
    override val fieldFaceMatch = "Face match"

    override val faceAuthentication = "Face Authentication"
    override val positionYourFace = "Position your face to begin"
    override val scanFace = "Scan Face"
    override val scanningFace = "Scanning Face"
    override val analyzingDataPoints = "ANALYZING 1,024 DATA POINTS…"
    override val holdStill = "Hold still…"
    override val matchConfirmed = "Match Confirmed"
    override val welcomeBackName = "Welcome back, Alex."
    override val fieldThreshold = "Threshold"
    override val fieldLiveness = "Liveness"
    override val fieldLatency = "Latency"
    override val passed = "Passed"

    override val auditLog = "Audit Log"
    override val filterAll = "All"
    override val filterSuccess = "Success"
    override val filterFailed = "Failed"
    override val filterDate = "Date"
    override val today = "Today"

    override val apiConfiguration = "API Configuration"
    override val endpointDemo = "Endpoint (demo)"
    override fun connectedLatency(ms: Int) = "Connected · ${ms}ms"
    override val preferences = "Preferences"
    override val theme = "Theme"
    override val darkMode = "Dark mode"
    override val biometricUnlock = "Biometric unlock"
    override val faceIdOnLaunch = "Face ID on launch"
    override val simulateFailure = "Simulate failure"
    override val forceVerificationToFail = "Force verification to fail"
    override val language = "Language"
    override val chooseLanguage = "Interface language"
    override val demoData = "Demo Data"
    override val resetDemoData = "Reset Demo Data"
    override val session = "Session"
    override val signedInAs = "Signed in as"
}

object Pt : Strings {
    override val done = "Concluir"
    override val tryAgain = "Tentar novamente"
    override val continueAction = "Continuar"
    override val contactSupport = "Falar com o suporte"
    override val logOut = "Sair"

    override val tabHome = "Início"
    override val tabVerify = "Verificar"
    override val tabFace = "Rosto"
    override val tabLogs = "Registos"

    override val tagline = "VERIFICAR · AUTENTICAR · CONFIAR"
    override val enterAccessCode = "INSIRA O CÓDIGO DE ACESSO"
    override val invalidCode = "Código inválido. Tente novamente."
    override val verify = "Verificar"
    override val verifying = "A verificar…"
    override fun endToEndEncrypted(version: String) = "ENCRIPTADO PONTA A PONTA · $version"

    override val welcomeBack = "Bem-vindo de volta"
    override val identityVerified = "Identidade Verificada"
    override fun trustScoreValid(score: Int) = "Pontuação de confiança $score · válida"
    override val modules = "Módulos"
    override val moduleIdentityVerification = "Verificação\nde Identidade"
    override val moduleFaceAuthentication = "Autenticação\nFacial"
    override val moduleLogsAudit = "Registos / Auditoria"
    override val settings = "Definições"

    override val verifyIdentity = "Verificar Identidade"
    override val confirmItsYou = "Vamos confirmar que é mesmo você"
    override val confirmItsYouSubtitle =
        "Uma verificação rápida em três passos. Tenha o seu documento de identificação à mão e boa iluminação."
    override val stepScanId = "Digitalize o seu documento"
    override val stepTakeSelfie = "Tire uma selfie rápida"
    override val stepLiveness = "Passe na verificação de vivacidade"
    override val secureDuration = "≈ 60 segundos · seguro"
    override val startVerification = "Iniciar Verificação"

    override val scanIdFront = "Digitalizar ID — Frente"
    override val scanIdBack = "Digitalizar ID — Verso"
    override val alignFrontOfId = "Alinhe a frente do documento no quadro"
    override val front = "Frente"
    override val back = "Verso"

    override val takeASelfie = "Tirar uma Selfie"
    override val centerYourFace = "Centre o seu rosto no oval"
    override val goodLightingDetected = "Boa iluminação detetada"

    override val livenessCheck = "Verificação de Vivacidade"
    override val turnYourHead = "Vire a cabeça lentamente →"
    override val keepFaceInsideCircle = "Mantenha o rosto dentro do círculo"
    override val blinkDetected = "Piscar detetado"
    override val smileDetected = "Sorriso detetado"
    override val turnHeadDetected = "Rotação da cabeça detetada"
    override val turnHeadInProgress = "Virar a cabeça — em curso"

    override val yourDocumentMatched = "O seu documento e biometria corresponderam com sucesso."
    override val trustScoreOutOf100 = "/100 de confiança"
    override val verificationFailed = "Verificação Falhou"
    override val couldntConfirmMatch = "Não foi possível confirmar uma correspondência. Reveja os problemas abaixo."
    override val issueBlurry = "Foto do documento demasiado desfocada"
    override val issueFaceBelowThreshold = "Correspondência facial abaixo do limite (61%)"
    override val issueLivenessInconclusive = "Vivacidade inconclusiva"
    override val fieldName = "Nome"
    override val fieldDocument = "Documento"
    override val fieldFaceMatch = "Correspondência facial"

    override val faceAuthentication = "Autenticação Facial"
    override val positionYourFace = "Posicione o seu rosto para começar"
    override val scanFace = "Digitalizar Rosto"
    override val scanningFace = "A Digitalizar Rosto"
    override val analyzingDataPoints = "A ANALISAR 1.024 PONTOS DE DADOS…"
    override val holdStill = "Não se mexa…"
    override val matchConfirmed = "Correspondência Confirmada"
    override val welcomeBackName = "Bem-vindo de volta, Alex."
    override val fieldThreshold = "Limite"
    override val fieldLiveness = "Vivacidade"
    override val fieldLatency = "Latência"
    override val passed = "Aprovado"

    override val auditLog = "Registo de Auditoria"
    override val filterAll = "Todos"
    override val filterSuccess = "Sucesso"
    override val filterFailed = "Falhados"
    override val filterDate = "Data"
    override val today = "Hoje"

    override val apiConfiguration = "Configuração da API"
    override val endpointDemo = "Endpoint (demo)"
    override fun connectedLatency(ms: Int) = "Ligado · ${ms}ms"
    override val preferences = "Preferências"
    override val theme = "Tema"
    override val darkMode = "Modo escuro"
    override val biometricUnlock = "Desbloqueio biométrico"
    override val faceIdOnLaunch = "Face ID ao iniciar"
    override val simulateFailure = "Simular falha"
    override val forceVerificationToFail = "Forçar a falha da verificação"
    override val language = "Idioma"
    override val chooseLanguage = "Idioma da interface"
    override val demoData = "Dados de Demonstração"
    override val resetDemoData = "Repor Dados de Demonstração"
    override val session = "Sessão"
    override val signedInAs = "Sessão iniciada como"
}

/** Resolve the [Strings] table for a [Language]. */
fun stringsFor(language: Language): Strings = when (language) {
    Language.English -> En
    Language.Portuguese -> Pt
}

/** Current UI copy. Provided in [com.identia.app.App] from the selected language. */
val LocalStrings = staticCompositionLocalOf<Strings> { En }
