package store.model.service;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.Test;
import store.Application;

class ReceiptCalculatorTest extends NsTest {

    @Test
    public void 정상_동작_테스트() throws Exception {
        assertSimpleTest(() -> {
            run("[콜라-12]", "Y", "Y", "N");
            assertThat(output().replaceAll("\\s", "")).contains("콜라1212", "콜라3", "총구매액1212,000", "행사할인-3,000",
                    "멤버십할인-900", "내실돈8,100");
        });
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}