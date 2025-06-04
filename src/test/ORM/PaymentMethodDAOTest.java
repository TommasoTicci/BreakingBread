package test.ORM;
import main.java.DomainModel.PaymentMethod;
import main.java.ORM.PaymentMethodDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class PaymentMethodDAOTest {
    private PaymentMethodDAO paymentMethodDAO;
    private PaymentMethod paymentMethod;

    @BeforeEach
    public void setUp() {
        paymentMethodDAO = new PaymentMethodDAO();
        paymentMethod = new PaymentMethod("nameTest","surnameName","64646464646464","10/25","456");
    }
    @AfterEach
    public void tearDown() {
        paymentMethodDAO.removePaymentMethod("64646464646464");

    }

    @Test
    public void testAddPaymentMethodTest()  {
        paymentMethodDAO.addPaymentMethod(paymentMethod);
        PaymentMethod paymentMethodDataBase = paymentMethodDAO.getPaymentMethod("64646464646464");
        assertEquals(paymentMethodDataBase.getOwnerName(),"nameTest");
    }




}
