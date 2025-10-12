package io.github.alberes.bank.wise.authorization.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.github.alberes.bank.wise.authorization.constants.Constants;
import io.github.alberes.bank.wise.authorization.domains.UserPrincipal;
import io.github.alberes.bank.wise.authorization.services.UserPrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityAuthorizationServerConfiguration {

    private final DateTimeFormatter formatter;

    @Value("${app.server.issuer}")
    private String issuer;

    @Value("${app.token.accessTokenExpiration}")
    private int accessTokenExpiration;

    @Value("${app.token.refreshTokenExpiration}")
    private int refreshTokenExpiration;

    //https://docs.spring.io/spring-authorization-server/reference/protocol-endpoints.html
    @Bean
    @Order(1)
    public SecurityFilterChain securityAuthorizationServer(HttpSecurity httpSecurity) throws Exception {
        OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer
                = OAuth2AuthorizationServerConfigurer.authorizationServer();
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .securityMatcher(oAuth2AuthorizationServerConfigurer.getEndpointsMatcher())
                .with(oAuth2AuthorizationServerConfigurer, (authorizationServer) ->
                        authorizationServer.oidc(Customizer.withDefaults()))
                .authorizeHttpRequests((authorize) ->
                        authorize.anyRequest().authenticated())
                .exceptionHandling((exceptions) ->
                        exceptions.defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint(Constants.SLASH_LOGIN),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        ));
        return httpSecurity.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityAuthorizationResource(HttpSecurity httpSecurity)
            throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) ->
                        authorize.anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .formLogin(Customizer.withDefaults())
                .build();

    }

    //An instance of com.nimbusds.jose.jwk.source.JWKSource for signing access tokens.
    @Bean
    public JWKSource<SecurityContext> jwkSource(){
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    //An instance of java.security.KeyPair with keys generated on startup used to create the JWKSource above.
    private KeyPair generateRsaKey() {
        KeyPair keyPair = null;
        try{
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(Constants.RSA);
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return keyPair;
    }

    //An instance of JwtDecoder for decoding signed access tokens.
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    //An instance of AuthorizationServerSettings to configure Spring Authorization Server.
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        if(Constants.EMPTY.equals(this.issuer.trim())){
            return AuthorizationServerSettings.builder().build();
        }else {
            return AuthorizationServerSettings.builder()
                    .issuer(this.issuer)
                    .build();
        }
    }

    @Bean
    public TokenSettings tokenSettings(){
        return TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.ofMinutes(this.accessTokenExpiration))
                .refreshTokenTimeToLive(Duration.ofMinutes(this.refreshTokenExpiration))
                .build();
    }

    @Bean
    public ClientSettings clientSettings(){
        return ClientSettings.builder()
                .requireAuthorizationConsent(false)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserPrincipalDetailsService service){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(this.passwordEncoder());
        provider.setUserDetailsService(service);
        return provider;
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtEncodingContext() {
        return context -> {
            Authentication authentication = context.getPrincipal();
            if(authentication.getPrincipal() instanceof  UserPrincipal userPrincipal) {
                OAuth2TokenType oauth2TokenType = context.getTokenType();
                if(OAuth2TokenType.ACCESS_TOKEN.equals(oauth2TokenType)){
                    JwtClaimsSet.Builder claims = context.getClaims();
                    claims.claim(Constants.ID, userPrincipal.getClientAccount().getId());
                    claims.claim(Constants.NAME, userPrincipal.getClientAccount().getName());
                    claims.claim(Constants.EMAIL, userPrincipal.getClientAccount().getLogin());
                    claims.claim(Constants.REGISTRATION_DATE, userPrincipal.getClientAccount().getCreatedDate()
                            .format(this.formatter));
                    claims.claim(Constants.SCOPE, userPrincipal.getClientAccount().getScopes());
                }
            }else if(authentication instanceof OAuth2ClientAuthenticationToken authorizationToken){
                RegisteredClient registeredClient = authorizationToken.getRegisteredClient();
                JwtClaimsSet.Builder claims = context.getClaims();
                claims.claim(Constants.ID, registeredClient.getId());
            }
        };
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring().requestMatchers(
                Constants.SWAGGERS.toArray(new String[Constants.SWAGGERS.size()])
        );
    }
}