package smiz.bw.components;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.LocalDate;

/**
 * JUnit test class.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DaysCalculator.class})
public class DaysCalculatorTest {

	@BeforeClass
	public static void setupMocks() {
		// LocalDate.now() call is mocked to always return Dec 1 2016
		LocalDate mockedNow = LocalDate.of(2016, 12, 1);

		PowerMockito.stub(PowerMockito.method(LocalDate.class, "now")).toReturn(mockedNow);
	}

	@Test
	public void test() {
		DaysCalculator dc = new DaysCalculator();

		Assert.assertTrue(dc.getDaysLeftUntil(12, 1) == 0);
		Assert.assertTrue(dc.getDaysLeftUntil(12, 2) == 1);
		Assert.assertTrue(dc.getDaysLeftUntil(11, 30) == 364);

		Assert.assertTrue(dc.getDaysLeftUntil(1, 1) == 31);
		Assert.assertTrue(dc.getDaysLeftUntil(2, 1) == 62);
		Assert.assertTrue(dc.getDaysLeftUntil(3, 1) == 90);
	}

}
