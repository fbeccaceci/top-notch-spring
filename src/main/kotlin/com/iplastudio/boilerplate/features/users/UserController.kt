package com.iplastudio.boilerplate.features.users

import com.iplastudio.boilerplate.features.users.dtos.*
import com.iplastudio.boilerplate.features.users.entities.User
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@Tag(name = "Users")
@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val userAccountActivationService: UserAccountActivationService,
    private val userPasswordResetService: UserPasswordResetService
) {

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@Valid @RequestBody request: UserRegistrationRequest) = userService.signUp(request)

    @PostMapping("/activate-account")
    fun activateAccount(@NotBlank @RequestParam otp: String) = userAccountActivationService.activateAccount(otp)

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: UsernameAndPasswordLoginRequest): UserAuthenticationResponse = userService.login(request)

    @PostMapping("/refresh-token")
    fun refreshToken(@Valid @RequestBody request: RefreshTokenRequest): UserAuthenticationResponse = userService.refreshToken(request)

    @GetMapping("/me")
    fun me(): User = userService.getLoggedUserOrThrow()

    @PutMapping("/request-reset-password")
    fun requestResetPassword(@Valid @RequestBody request: ResetPasswordRequest) = userPasswordResetService.requestPasswordReset(request)

    @PutMapping("/reset-password")
    fun resetPassword(@Valid @RequestBody request: ConfirmPasswordResetRequest) = userPasswordResetService.confirmPasswordReset(request)

}