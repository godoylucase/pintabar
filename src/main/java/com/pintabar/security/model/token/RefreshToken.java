package com.pintabar.security.model.token;

import com.pintabar.security.model.Scopes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.List;
import java.util.Optional;

/**
 * Created by lucasgodoy on 9/06/17.
 */
public class RefreshToken implements JwtToken {
	private Jws<Claims> claims;

	private RefreshToken(Jws<Claims> claims) {
		this.claims = claims;
	}

	/**
	 * Creates and validates Refresh token
	 *
	 * @param token
	 * @param signingKey
	 *
	 * @throws BadCredentialsException
	 * @throws JwtExpiredTokenException
	 *
	 * @return
	 */
	public static Optional<RefreshToken> create(RawAccessJwtToken token, String signingKey) {
		Jws<Claims> claims = token.parseClaims(signingKey);

		List<String> scopes = claims.getBody().get("scopes", List.class);
		if (scopes == null || scopes.isEmpty()
				|| scopes.stream()
				.noneMatch(scope -> Scopes.REFRESH_TOKEN.authority().equals(scope))) {
			return Optional.empty();
		}

		return Optional.of(new RefreshToken(claims));
	}

	@Override
	public String getToken() {
		return null;
	}

	public Jws<Claims> getClaims() {
		return claims;
	}

	public String getJti() {
		return claims.getBody().getId();
	}

	public String getSubject() {
		return claims.getBody().getSubject();
	}
}
