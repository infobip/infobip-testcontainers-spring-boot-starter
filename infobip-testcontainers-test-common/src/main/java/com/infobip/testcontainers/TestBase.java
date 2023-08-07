package com.infobip.testcontainers;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = Main.class)
public class TestBase {

}
