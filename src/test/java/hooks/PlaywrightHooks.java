package hooks;

import drivers.PlaywrightFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.qameta.allure.Allure;

public class PlaywrightHooks {

    @Before
    public void setup(io.cucumber.java.Scenario scenario) {
        Allure.step("Starting scenario: " + scenario.getName());
        PlaywrightFactory.getPage();
    }




    @After
    public void teardown(io.cucumber.java.Scenario scenario) {
        if (scenario.isFailed()) {
            byte[] screenshot = PlaywrightFactory.getPage().screenshot();
            Allure.addAttachment("Failure Screenshot", "image/png",
                    new java.io.ByteArrayInputStream(screenshot), "png");
        }

        PlaywrightFactory.close();
    }


}
