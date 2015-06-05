package alfa;

import org.junit.*;
import tools.Log;

/**
 * Created by ledenev.p on 31.03.2015.
 */

@Ignore
public class RealRequestTest {

    AlfaGateway gateway;

    @Before
    public void setUp() throws Throwable {
        gateway = new AlfaGateway("pledenev", "");
    }

    @Test
    public void loadVersion() throws Throwable {
        Log.info(gateway.loadVersion());
    }
}
