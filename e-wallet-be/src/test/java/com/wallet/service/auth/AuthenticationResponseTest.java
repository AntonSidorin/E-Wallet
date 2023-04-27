package com.wallet.service.auth;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;
import static pl.pojo.tester.api.assertion.Method.CONSTRUCTOR;
import static pl.pojo.tester.api.assertion.Method.EQUALS;
import static pl.pojo.tester.api.assertion.Method.GETTER;
import static pl.pojo.tester.api.assertion.Method.HASH_CODE;
import static pl.pojo.tester.api.assertion.Method.TO_STRING;

import org.junit.jupiter.api.Test;
import pl.pojo.tester.api.assertion.Method;

class AuthenticationResponseTest {

    @Test
    void AuthenticationResponsePojoClassShouldBeWellImplemented() {
        var methods = new Method[]{CONSTRUCTOR, GETTER, EQUALS, HASH_CODE, TO_STRING};
        assertPojoMethodsFor(AuthenticationResponse.class).testing(methods).areWellImplemented();
    }

}
