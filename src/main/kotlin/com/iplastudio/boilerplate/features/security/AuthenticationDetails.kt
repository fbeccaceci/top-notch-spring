package com.iplastudio.boilerplate.features.security

import java.util.UUID

data class AuthenticationDetails(val username: String, val userId: UUID)