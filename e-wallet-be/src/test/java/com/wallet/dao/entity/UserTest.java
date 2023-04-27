package com.wallet.dao.entity;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;
import static pl.pojo.tester.api.assertion.Method.CONSTRUCTOR;
import static pl.pojo.tester.api.assertion.Method.EQUALS;
import static pl.pojo.tester.api.assertion.Method.GETTER;

import org.junit.jupiter.api.Test;
import pl.pojo.tester.api.assertion.Method;

class UserTest {

    @Test
    void userPojoClassShouldBeWellImplemented() {
        var methods = new Method[]{CONSTRUCTOR, GETTER, EQUALS};
        assertPojoMethodsFor(User.class).testing(methods).areWellImplemented();
    }

}
