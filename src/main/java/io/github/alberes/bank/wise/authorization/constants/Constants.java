package io.github.alberes.bank.wise.authorization.constants;

import java.util.Set;

public interface Constants {

    public static final String DATE_TIME_FORMATTER_PATTERN = "dd-MM-yyyy HH:mm:ss";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String REGISTRATION_DATE = "registrationDate";
    public static final String OBJECT_NOT_FOUND = "Object not found!";
    public static final String SLASH_LOGIN = "/login";
    public static final String RSA = "RSA";
    public static final String SCOPE = "scope";
    public static final String EMPTY = "";

    public static String _OR_ = " or ";
    public static final String HAS_ROLE_ADMIN = "hasRole('ADMIN')";
    public static final String HAS_CLIENT_AUTHORITY_WRITE = "hasAuthority('WRITE')";
    public static final String HAS_CLIENT_AUTHORITY_READ = "hasAuthority('READ')";
    public static final String HAS_CLIENT_AUTHORITY_UPDATE = "hasAuthority('UPDATE')";
    public static final String HAS_CLIENT_AUTHORITY_DELETE = "hasAuthority('DELETE')";
    public static final String UNAUTHORIZED_MESSAGE = "The user can only access resources that belong to him.";
    public static final String SLASH = "/";
    public static final String SPACE = " ";
    public static final String BLANK = "";
    public static final String REGISTRATION_WITH_LOGIN_OR_LEGAL_ENTITY_NUMBER = "Registration with LOGIN or Legal entity number";
    public static final String REGISTRATION_WITH_E_CLIENT_ID = "Registration with client-id ";
    public static final String HAS_ALREADY_BEEN_REGISTERED = " has already been registered!";
    public static final String OBJECT_NOT_FOUND_ID = "Object not found! Id: ";
    public static final String TYPE = ", Type: ";
    public static final Set<String> SWAGGERS = Set.of("/v2/api-docs/**", "/v3/api-docs/**", "/swagger-resources/**",
            "/swagger-ui.html", "/swagger-ui/**", "/webjars/**", "/actuator/**", "/h2-console", "/h2-console/**");

    public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "access-control-expose-headers";
    public static final String LOCATION = "location";
}
