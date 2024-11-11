package store.model.service;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import store.Application;
import camp.nextstep.edu.missionutils.test.NsTest;

class PurchaseProcessingTest extends NsTest {

    @Test
    public void 프로모션_혜택은_프로모션_재고_내에서만_적용할_수_있다() throws Exception {

        assertSimpleTest(() -> {
            run("[콜라-12]", "Y", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("총구매액1212,000", "행사할인-3,000", "멤버십할인-0", "내실돈9,000");
        });
    }

    @Test
    public void 프로모션_혜택은_프로모션_재고를_우선적으로_차감하며_프로모션_재고가_부족할_경우_일반재고를_사용한다() throws Exception {

        assertSimpleTest(() -> {
            run("[콜라-12]", "Y", "N", "Y", "[콜라-1]", "Y", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("총구매액1212,000", "행사할인-3,000", "멤버십할인-0", "내실돈9,000");
        });
    }


    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}