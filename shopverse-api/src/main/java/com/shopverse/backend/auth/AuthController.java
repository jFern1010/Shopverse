package com.shopverse.backend.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopverse.backend.models.Role;
import com.shopverse.backend.models.User;
import com.shopverse.backend.repositories.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/shopverse/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {

	private final AuthenticationManager authManager;
	private final JwtEncoder jwtEncoder;
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepo;

	public AuthController(AuthenticationManager authManager, JwtEncoder jwtEncoder, PasswordEncoder passwordEncoder,
			UserRepository userRepo) {
		this.authManager = authManager;
		this.jwtEncoder = jwtEncoder;
		this.passwordEncoder = passwordEncoder;
		this.userRepo = userRepo;
	}

	@PostMapping("/register")
	@Operation(summary = "Register a new user")
	public ResponseEntity<String> register(@Valid @RequestBody User user) {

		Optional<User> existingUser = userRepo.findByEmail(user.getEmail());
		if (existingUser.isPresent()) {
			throw new RuntimeException("Email already exists");
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.getRoles().add(Role.USER);
		userRepo.save(user);

		return ResponseEntity.ok("User registered successfully");
	}

	@PostMapping("/login")
	@Operation(summary = "Login and receive a JWT token")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
		var auth = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

		Instant now = Instant.now();
		long expiry = 3600L;
		String token = jwtEncoder
				.encode(JwtEncoderParameters.from(JwtClaimsSet
						.builder().issuer("shopverse").issuedAt(now).expiresAt(now.plus(6, ChronoUnit.HOURS))
						.subject(auth.getName()).claim("scope", auth.getAuthorities().stream()
								.map(GrantedAuthority::getAuthority).collect(Collectors.joining(" ")))
						.build()))
				.getTokenValue();

		User user = userRepo.findByEmail(request.email()).orElseThrow(() -> new RuntimeException("User not found"));

		return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getEmail()));

	}

}

record AuthRequest(String email, String password) {

}

record AuthResponse(String token, Long id, String email) {

}