package com.pluu.navigator.util

import org.junit.Assert.assertEquals
import org.junit.Test

class ClassExtTest {
    @Test
    fun enclosingClassFullSimpleName() {
      assertEquals("A", A().javaClass.enclosingClassFullSimpleName())
      assertEquals("A\$B", A.B().javaClass.enclosingClassFullSimpleName())
      assertEquals("A\$B\$C", A.B.C().javaClass.enclosingClassFullSimpleName())
    }
}

class A {
    class B {
        class C
    }
}