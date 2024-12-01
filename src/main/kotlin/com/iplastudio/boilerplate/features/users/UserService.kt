package com.iplastudio.boilerplate.features.users

import com.iplastudio.boilerplate.exceptions.EntityNotFoundException
import com.iplastudio.boilerplate.features.security.AuthenticationDetailsHolder
import com.iplastudio.boilerplate.features.security.exceptions.InvalidAuthenticationDetailsException
import com.iplastudio.boilerplate.features.security.exceptions.MissingPrincipalException
import com.iplastudio.boilerplate.features.security.jwt.JwtService
import com.iplastudio.boilerplate.features.security.jwt.RefreshTokenService
import com.iplastudio.boilerplate.features.users.dtos.RefreshTokenRequest
import com.iplastudio.boilerplate.features.users.dtos.UserAuthenticationResponse
import com.iplastudio.boilerplate.features.users.dtos.UserRegistrationRequest
import com.iplastudio.boilerplate.features.users.dtos.UsernameAndPasswordLoginRequest
import com.iplastudio.boilerplate.features.users.entities.User
import com.iplastudio.boilerplate.features.users.exceptions.PasswordDontMatchException
import com.iplastudio.boilerplate.features.users.exceptions.TokenRefreshFailedException
import com.iplastudio.boilerplate.features.users.exceptions.UserNotEnabledException
import com.iplastudio.boilerplate.features.users.exceptions.UsernameOrEmailAlreadyInUseException
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val userAccountActivationService: UserAccountActivationService,
    private val jwtService: JwtService,
    private val refreshTokenService: RefreshTokenService
) {

    private val log = LoggerFactory.getLogger(UserService::class.java)

    fun signUp(request: UserRegistrationRequest) {
        if (userRepository.existsByUsernameOrEmail(request.username, request.email)) {
            throw UsernameOrEmailAlreadyInUseException()
        }

        log.info("Signing up user with username ${request.username}")

        var newUser = User(
            username = request.username,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            status = User.Status.WAITING_EMAIL_VERIFICATION,
            locale = request.locale ?: Locale.ENGLISH
        )

        newUser = userRepository.save(newUser)

        userAccountActivationService.beginAccountActivation(newUser)
    }

    fun login(request: UsernameAndPasswordLoginRequest): UserAuthenticationResponse {
        val user = userRepository.findByEmailOrUsername(request.username, request.username)
            ?: throw EntityNotFoundException(User::class)

        // We check first for the password match, and then for the user active status so in the login flow
        // If the client gets a user not enabled error, it can prompt the user to insert the otp and enable the account
        // If we check first for the activation status, the client will prompt the user to insert the otp and enable the account
        // Event if the password is wrong, the user will be able to log in with the otp
        if (!passwordEncoder.matches(request.password, user.password)) {
            throw PasswordDontMatchException()
        }

        if (user.status !== User.Status.ACTIVE) {
            throw UserNotEnabledException()
        }

        log.info("User ${user.username} logged in")
        val token = jwtService.generateToken(user)
        val refreshToken = refreshTokenService.getForUser(user.id!!)
        return UserAuthenticationResponse(token, refreshToken.token)
    }

    fun refreshToken(request: RefreshTokenRequest): UserAuthenticationResponse {
        try {
            val refreshToken = refreshTokenService.verify(request.token)
            val user = userRepository.findByIdOrNull(refreshToken.userId)
                ?: throw EntityNotFoundException(User::class);

            val token = jwtService.generateToken(user)
            return UserAuthenticationResponse(token, refreshToken.token)
        } catch (e: Exception) {
            throw TokenRefreshFailedException(e)
        }
    }

    /**
     * Get the logged user or throw an exception
     * To be used in places where authentication is required and proceeding without the logged user wouldn't be possible
     */
    fun getLoggedUserOrThrow(): User {
        val loggedUserUsername = AuthenticationDetailsHolder.details ?: throw MissingPrincipalException()
        return userRepository.findByUsername(loggedUserUsername.username)
            ?: throw InvalidAuthenticationDetailsException(
                details = "Username in principal doesn't match any user"
            )
    }

}